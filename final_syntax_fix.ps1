# final_syntax_fix.ps1
# Removes double closing parens in connections = listOf(...)

$files = Get-ChildItem "shared\src\commonMain\kotlin\com\jalmarquest\shared\world\catalog\LocationCatalog_*.kt"

Write-Host "Final syntax cleanup..." -ForegroundColor Cyan

foreach ($file in $files) {
    Write-Host "Processing $($file.Name)..." -ForegroundColor Yellow
    
    $content = Get-Content $file.FullName -Raw
    
    # Fix double closing parens: LocationConnection(...)))\n            ),
    # Should be: LocationConnection(...)),\n            ),
    # Actually, pattern is: Direction.DIR))\n            ),
    # Should be: Direction.DIR)\n            ),
    
    $content = $content -replace '(Direction\.\w+)\)\)', '$1)'
    
    Set-Content -Path $file.FullName -Value $content -NoNewline
    
    Write-Host "  Saved $($file.Name)" -ForegroundColor Green
}

Write-Host ""
Write-Host "Cleanup complete." -ForegroundColor Green
