# apply_fixes_simple.ps1
# Simpler line-by-line approach to add missing reciprocal connections

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

# Direction opposites
$opposites = @{
    "NORTH" = "SOUTH"; "SOUTH" = "NORTH"
    "EAST" = "WEST"; "WEST" = "EAST"
    "NORTHEAST" = "SOUTHWEST"; "SOUTHWEST" = "NORTHEAST"
    "NORTHWEST" = "SOUTHEAST"; "SOUTHEAST" = "NORTHWEST"
    "UP" = "DOWN"; "DOWN" = "UP"
}

Write-Host "Building location graph..." -ForegroundColor Cyan

# Parse all locations and their connections
$locations = @{}

foreach ($file in $catalogFiles) {
    $lines = Get-Content $file
    $currentId = $null
    $inConnections = $false
    $connections = @()
    
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        
        # Match: id = "location_id"
        if ($line -match 'id\s*=\s*"([^"]+)"') {
            $currentId = $matches[1]
            $connections = @()
            $inConnections = $false
        }
        
        # Match: connections = listOf(
        if ($line -match 'connections\s*=\s*listOf\(') {
            $inConnections = $true
            
            # Check if connections close on same line
            if ($line -match '\)[\s,]*$') {
                $inConnections = $false
            }
            continue
        }
        
        # Inside connections block - match: LocationConnection("target", Direction.DIR)
        if ($inConnections) {
            if ($line -match 'LocationConnection\s*\(\s*"([^"]+)"\s*,\s*Direction\.(\w+)') {
                $connections += @{
                    Target = $matches[1]
                    Direction = $matches[2]
                }
            }
            
            # Check if connections block closes
            if ($line -match '^\s*\)[\s,]*$') {
                $inConnections = $false
                
                # Save location
                if ($currentId) {
                    $locations[$currentId] = @{
                        File = $file
                        Connections = $connections
                    }
                }
            }
        }
    }
}

Write-Host "Parsed $($locations.Count) locations" -ForegroundColor Green

# Find missing reciprocals
$missingByLocation = @{}

foreach ($locId in $locations.Keys) {
    $loc = $locations[$locId]
    
    foreach ($conn in $loc.Connections) {
        $targetId = $conn.Target
        $direction = $conn.Direction
        $oppositeDir = $opposites[$direction]
        
        if (-not $oppositeDir) { continue }
        if (-not $locations.ContainsKey($targetId)) { continue }
        
        $targetLoc = $locations[$targetId]
        
        # Check if reciprocal exists
        $hasReciprocal = $false
        foreach ($targetConn in $targetLoc.Connections) {
            if ($targetConn.Target -eq $locId -and $targetConn.Direction -eq $oppositeDir) {
                $hasReciprocal = $true
                break
            }
        }
        
        if (-not $hasReciprocal) {
            if (-not $missingByLocation.ContainsKey($targetId)) {
                $missingByLocation[$targetId] = @{
                    File = $targetLoc.File
                    Fixes = @()
                }
            }
            
            $missingByLocation[$targetId].Fixes += @{
                Target = $locId
                Direction = $oppositeDir
            }
        }
    }
}

Write-Host "Found $($missingByLocation.Count) locations needing fixes" -ForegroundColor Yellow
Write-Host ""

# Apply fixes file by file
$fixesByFile = @{}
foreach ($locId in $missingByLocation.Keys) {
    $entry = $missingByLocation[$locId]
    $file = $entry.File
    
    if (-not $fixesByFile.ContainsKey($file)) {
        $fixesByFile[$file] = @{}
    }
    
    $fixesByFile[$file][$locId] = $entry.Fixes
}

$totalFixed = 0

foreach ($file in $fixesByFile.Keys) {
    Write-Host "Processing $([System.IO.Path]::GetFileName($file))..." -ForegroundColor Cyan
    
    $content = Get-Content $file -Raw
    $locationsFixed = 0
    
    foreach ($locId in $fixesByFile[$file].Keys) {
        $fixes = $fixesByFile[$file][$locId]
        
        # Find the connections = listOf(...) block for this location
        $idPattern = [regex]::Escape($locId)
        
        # Match from id = "xxx" to the connections closing paren
        if ($content -match "id\s*=\s*`"$idPattern`"[\s\S]*?connections\s*=\s*listOf\(([\s\S]*?)\s*\)") {
            $oldConnBlock = $matches[1]
            $newConnBlock = $oldConnBlock
            
            # Add each missing connection
            foreach ($fix in $fixes) {
                $newEntry = "LocationConnection(`"$($fix.Target)`", Direction.$($fix.Direction))"
                
                # Add comma if needed
                if ($newConnBlock.Trim() -ne "") {
                    if ($newConnBlock.Trim() -notmatch ',\s*$') {
                        $newConnBlock = $newConnBlock.TrimEnd() + ","
                    }
                    $newConnBlock += "`n                $newEntry"
                } else {
                    $newConnBlock = "`n                $newEntry`n            "
                }
            }
            
            # Replace old with new
            $oldFull = $matches[0]
            $newFull = $oldFull -replace [regex]::Escape($oldConnBlock), $newConnBlock
            
            $content = $content -replace [regex]::Escape($oldFull), $newFull
            
            $locationsFixed++
            $totalFixed += $fixes.Count
            Write-Host "  ✓ $locId (+$($fixes.Count))" -ForegroundColor Green
        }
        else {
            Write-Host "  ✗ Could not find connections block for $locId" -ForegroundColor Red
        }
    }
    
    # Write back
    Set-Content -Path $file -Value $content -NoNewline
    Write-Host "  Saved ($locationsFixed locations fixed)" -ForegroundColor Green
    Write-Host ""
}

Write-Host "=" * 60 -ForegroundColor Cyan
Write-Host "COMPLETE: Added $totalFixed reciprocal connections" -ForegroundColor Green
Write-Host "=" * 60 -ForegroundColor Cyan
