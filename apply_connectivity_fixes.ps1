# apply_connectivity_fixes.ps1
# Automatically adds missing reciprocal connections to regional location catalogs

$catalogFiles = @(
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Cave.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Coastal.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Desert.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Forest.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Grassland.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Mountain.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Swamp.kt",
    "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_Tundra.kt"
)

# Direction opposites mapping
$opposites = @{
    "NORTH" = "SOUTH"; "SOUTH" = "NORTH"
    "EAST" = "WEST"; "WEST" = "EAST"
    "NORTHEAST" = "SOUTHWEST"; "SOUTHWEST" = "NORTHEAST"
    "NORTHWEST" = "SOUTHEAST"; "SOUTHEAST" = "NORTHWEST"
    "UP" = "DOWN"; "DOWN" = "UP"
}

Write-Host "Starting automated connectivity fix process..." -ForegroundColor Cyan
Write-Host ""

# Build comprehensive location graph from all catalogs
$locations = @{}
$allConnections = @()

foreach ($file in $catalogFiles) {
    $content = Get-Content $file -Raw
    
    # Extract all locations with their connections
    # Pattern: Location( ... id = "xxx" ... connections = listOf(...) ... )
    $pattern = 'Location\s*\([^)]*?id\s*=\s*"([^"]+)"[^)]*?connections\s*=\s*listOf\(((?:[^()]|\([^)]*\))*?)\)[^)]*?\)'
    $matches = [regex]::Matches($content, $pattern)
    
    foreach ($match in $matches) {
        $locationId = $match.Groups[1].Value
        $connectionsBlock = $match.Groups[2].Value
        
        # Extract individual connections
        $connPattern = 'LocationConnection\s*\(\s*"([^"]+)"\s*,\s*Direction\.(\w+)\s*\)'
        $connMatches = [regex]::Matches($connectionsBlock, $connPattern)
        
        $connections = @()
        foreach ($connMatch in $connMatches) {
            $targetId = $connMatch.Groups[1].Value
            $direction = $connMatch.Groups[2].Value
            $connections += @{ Target = $targetId; Direction = $direction }
            
            # Track all connections for analysis
            $allConnections += @{
                From = $locationId
                To = $targetId
                Direction = $direction
                File = $file
            }
        }
        
        if (-not $locations.ContainsKey($locationId)) {
            $locations[$locationId] = @{
                File = $file
                Connections = @()
            }
        }
        $locations[$locationId].Connections = $connections
    }
}

Write-Host "Parsed $($locations.Count) locations from catalogs" -ForegroundColor Green
Write-Host "Found $($allConnections.Count) existing connections" -ForegroundColor Green
Write-Host ""

# Find and apply missing reciprocals
$fixesByFile = @{}
$totalFixed = 0

foreach ($conn in $allConnections) {
    $fromId = $conn.From
    $toId = $conn.To
    $direction = $conn.Direction
    $oppositeDir = $opposites[$direction]
    
    if (-not $oppositeDir) { continue }  # Skip if no opposite direction
    if (-not $locations.ContainsKey($toId)) { continue }  # Skip if target doesn't exist
    
    # Check if reciprocal exists
    $targetLocation = $locations[$toId]
    $hasReciprocal = $false
    foreach ($existingConn in $targetLocation.Connections) {
        if ($existingConn.Target -eq $fromId -and $existingConn.Direction -eq $oppositeDir) {
            $hasReciprocal = $true
            break
        }
    }
    
    if (-not $hasReciprocal) {
        # Need to add reciprocal connection
        $targetFile = $targetLocation.File
        
        if (-not $fixesByFile.ContainsKey($targetFile)) {
            $fixesByFile[$targetFile] = @{}
        }
        
        if (-not $fixesByFile[$targetFile].ContainsKey($toId)) {
            $fixesByFile[$targetFile][$toId] = @()
        }
        
        # Add missing connection to fix list
        $fixesByFile[$targetFile][$toId] += @{
            Target = $fromId
            Direction = $oppositeDir
        }
        
        # Also add to in-memory location to prevent duplicate fixes
        $targetLocation.Connections += @{ Target = $fromId; Direction = $oppositeDir }
        $totalFixed++
    }
}

Write-Host "Identified $totalFixed missing reciprocal connections" -ForegroundColor Yellow
Write-Host ""

# Apply fixes to each file
$filesModified = 0

foreach ($file in $fixesByFile.Keys) {
    $fileFixes = $fixesByFile[$file]
    $content = Get-Content $file -Raw
    
    Write-Host "Processing $file..." -ForegroundColor Cyan
    Write-Host "  Locations to fix: $($fileFixes.Count)" -ForegroundColor White
    
    foreach ($locationId in $fileFixes.Keys) {
        $newConnections = $fileFixes[$locationId]
        
        # Find the connections block for this location
        $locationPattern = "Location\s*\(\s*id\s*=\s*`"$([regex]::Escape($locationId))`"[\s\S]*?connections\s*=\s*listOf\(([\s\S]*?)\)"
        $locationMatch = [regex]::Match($content, $locationPattern)
        
        if (-not $locationMatch.Success) {
            Write-Host "  WARNING: Could not find location $locationId" -ForegroundColor Red
            continue
        }
        
        $oldConnectionsBlock = $locationMatch.Groups[1].Value
        $newConnectionsBlock = $oldConnectionsBlock
        
        # Add new connections to the end of the list
        foreach ($newConn in $newConnections) {
            $newConnEntry = "LocationConnection(`"$($newConn.Target)`", Direction.$($newConn.Direction))"
            
            # Check if this is the last item or if we need a comma
            if ($oldConnectionsBlock.Trim() -eq "") {
                # Empty list - just add the connection
                $newConnectionsBlock = "`n            $newConnEntry`n        "
            } else {
                # Add comma and new connection
                if ($newConnectionsBlock.Trim() -notmatch ',\s*$') {
                    $newConnectionsBlock = $newConnectionsBlock.TrimEnd() + ","
                }
                $newConnectionsBlock += "`n            $newConnEntry"
            }
        }
        
        # Replace old connections block with new one
        $oldFullBlock = $locationMatch.Groups[0].Value
        $newFullBlock = $oldFullBlock -replace [regex]::Escape($oldConnectionsBlock), $newConnectionsBlock
        
        $content = $content -replace [regex]::Escape($oldFullBlock), $newFullBlock
        
        Write-Host "  ✓ Fixed $locationId (added $($newConnections.Count) connections)" -ForegroundColor Green
    }
    
    # Write back to file
    Set-Content -Path $file -Value $content -NoNewline
    $filesModified++
    Write-Host "  Saved $file" -ForegroundColor Green
    Write-Host ""
}

Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host "CONNECTIVITY FIX COMPLETE" -ForegroundColor Green
Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host "Files modified: $filesModified" -ForegroundColor White
Write-Host "Total connections added: $totalFixed" -ForegroundColor White
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Build project: .\gradlew :shared:desktopTest" -ForegroundColor White
Write-Host "2. Run WorldConnectivityTest to validate 100% reachability" -ForegroundColor White
