# Script to help place the JalmarQuest background image
#
# INSTRUCTIONS:
# 1. Save the beautiful JalmarQuest artwork (the one with Jalmar and the blue butterfly)
#    to your Downloads folder or Desktop as "jalmarquest_background.png"
#
# 2. Update the $sourcePath variable below to point to where you saved it
#
# 3. Run this script in PowerShell from the project root

# UPDATE THIS PATH to where you saved the image:
$sourcePath = "$env:USERPROFILE\Downloads\jalmarquest_background.png"
# Or if on Desktop:
# $sourcePath = "$env:USERPROFILE\Desktop\jalmarquest_background.png"

$destinationPath = ".\composeApp\src\commonMain\resources\jalmarquest_background.png"

if (Test-Path $sourcePath) {
    Copy-Item -Path $sourcePath -Destination $destinationPath -Force
    Write-Host "✅ SUCCESS! Image copied to: $destinationPath" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Uncomment the Image() code in MainMenuScreen.kt" -ForegroundColor Yellow
    Write-Host "2. Remove the gradient Box code" -ForegroundColor Yellow
    Write-Host "3. Recompile and run the app" -ForegroundColor Yellow
} else {
    Write-Host "❌ ERROR: Image not found at: $sourcePath" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please:" -ForegroundColor Yellow
    Write-Host "1. Save the JalmarQuest artwork image" -ForegroundColor White
    Write-Host "2. Update the `$sourcePath variable in this script" -ForegroundColor White
    Write-Host "3. Run this script again" -ForegroundColor White
}
