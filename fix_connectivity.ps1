# Script to find missing reciprocal connections in LocationCatalogs
# This identifies one-way connections that should be bidirectional

$catalogPath = "d:\JalmarQuest\Jalmar\shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog"
$catalogFiles = Get-ChildItem "$catalogPath\LocationCatalog_*.kt"

# Direction opposites for reciprocal connections
$opposites = @{
    "NORTH" = "SOUTH"
    "SOUTH" = "NORTH"
    "EAST" = "WEST"
    "WEST" = "EAST"
    "NORTHEAST" = "SOUTHWEST"
    "SOUTHWEST" = "NORTHEAST"
    "NORTHWEST" = "SOUTHEAST"
    "SOUTHEAST" = "NORTHWEST"
    "UP" = "DOWN"
    "DOWN" = "UP"
}

# Parse all locations and their connections
$locations = @{}

foreach ($file in $catalogFiles) {
    $content = Get-Content $file.FullName -Raw
    
    # Extract location blocks with id and connections
    $pattern = 'id = "([^"]+)"[\s\S]*?connections = listOf\(([\s\S]*?)\)'
    $matches = [regex]::Matches($content, $pattern)
    
    foreach ($match in $matches) {
        $locationId = $match.Groups[1].Value
        $connectionsBlock = $match.Groups[2].Value
        
        # Extract individual connections
        $connPattern = 'LocationConnection\("([^"]+)",\s*Direction\.(\w+)'
        $connMatches = [regex]::Matches($connectionsBlock, $connPattern)
        
        $connections = @()
        foreach ($connMatch in $connMatches) {
            $connections += @{
                Target = $connMatch.Groups[1].Value
                Direction = $connMatch.Groups[2].Value
            }
        }
        
        $locations[$locationId] = @{
            File = $file.Name
            Connections = $connections
        }
    }
}

Write-Host "Parsed $($locations.Count) locations from catalogs"
Write-Host ""

# Find missing reciprocal connections
$missingConnections = @()

foreach ($locId in $locations.Keys) {
    $loc = $locations[$locId]
    
    foreach ($conn in $loc.Connections) {
        $targetId = $conn.Target
        $direction = $conn.Direction
        $oppositeDir = $opposites[$direction]
        
        if (-not $oppositeDir) {
            continue  # Skip if no opposite (shouldn't happen)
        }
        
        # Check if target location exists
        if (-not $locations.ContainsKey($targetId)) {
            continue  # Target in base catalog or doesn't exist
        }
        
        # Check if target has reciprocal connection
        $targetLoc = $locations[$targetId]
        $hasReciprocal = $false
        
        foreach ($targetConn in $targetLoc.Connections) {
            if ($targetConn.Target -eq $locId -and $targetConn.Direction -eq $oppositeDir) {
                $hasReciprocal = $true
                break
            }
        }
        
        if (-not $hasReciprocal) {
            $missingConnections += @{
                From = $targetId
                To = $locId
                Direction = $oppositeDir
                File = $targetLoc.File
            }
        }
    }
}

Write-Host "Found $($missingConnections.Count) missing reciprocal connections:"
Write-Host ""

# Group by file
$byFile = $missingConnections | Group-Object -Property File

foreach ($group in $byFile) {
    Write-Host "=== $($group.Name) ==="
    foreach ($missing in $group.Group) {
        Write-Host "  $($missing.From) -> $($missing.To) (Direction.$($missing.Direction))"
    }
    Write-Host ""
}

# Output summary for import into batch fix
Write-Host "Summary: $($missingConnections.Count) missing connections across $($byFile.Count) files"
