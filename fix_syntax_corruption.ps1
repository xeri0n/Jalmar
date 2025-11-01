# fix_syntax_corruption.ps1
# Repairs corrupted LocationConnection syntax in catalog files

$files = Get-ChildItem "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_*.kt"

Write-Host "Repairing syntax corruption in $($files.Count) files..." -ForegroundColor Cyan

foreach ($file in $files) {
    Write-Host "Processing $($file.Name)..." -ForegroundColor Yellow
    
    $content = Get-Content $file.FullName -Raw
    
    # Fix literal \n strings that should be actual newlines
    $content = $content -replace '\\n', "`n"
    
    # Fix pattern: LocationConnection(...),\n                LocationConnection where \n is now actual newline
    # Already mostly fixed by above, but clean up any remaining issues
    
    Set-Content -Path $file.FullName -Value $content -NoNewline
    
    Write-Host "  Saved $($file.Name)" -ForegroundColor Green
}

Write-Host ""
Write-Host "Repair complete. Now run: .\gradlew :shared:desktopTest" -ForegroundColor Cyan
