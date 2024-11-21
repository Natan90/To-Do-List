
$scriptDirectory = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
$localVersionFile = Join-Path $scriptDirectory "version.txt"
$localJarPath = Join-Path $scriptDirectory "target\demo-1.0-SNAPSHOT.jar"
$tempDownloadPath = Join-Path $scriptDirectory "target\demo-latest.jar"

$githubRepo = "https://github.com/Natan90/To-Do-List.git"

# Commandes Java
$javaCommand = "java"
$javaOptions = "-jar"

function Get-RemoteVersion {
    try {
        $response = Invoke-RestMethod -Uri $githubRepo -Method Get
        return $response.tag_name
    } catch {
        Write-Host "Erreur : Impossible de récupérer la version distante sur GitHub." -ForegroundColor Red
        return $null
    }
}

function Get-LocalVersion {
    if (Test-Path $localVersionFile) {
        return Get-Content $localVersionFile
    } else {
        Write-Host "Le fichier de version locale est introuvable. Aucune version installée n'est supposée." -ForegroundColor Yellow
        return "0.0.0"
    }
}

function Update-Application {
    Write-Host "Téléchargement de la dernière version..." -ForegroundColor Cyan
    $response = Invoke-RestMethod -Uri $githubRepo -Method Get
    $downloadUrl = $response.assets | Where-Object { $_.name -match "\.jar$" } | Select-Object -ExpandProperty browser_download_url

    if ($downloadUrl) {
        Invoke-WebRequest -Uri $downloadUrl -OutFile $tempDownloadPath -UseBasicParsing
        Write-Host "Téléchargement terminé. Mise à jour de l'application..." -ForegroundColor Green
        Move-Item -Force -Path $tempDownloadPath -Destination $localJarPath
        $remoteVersion = $response.tag_name
        Set-Content -Path $localVersionFile -Value $remoteVersion
        Write-Host "Application mise à jour vers la version $remoteVersion." -ForegroundColor Green
    } else {
        Write-Host "Erreur : Impossible de trouver le fichier JAR dans les assets de la release." -ForegroundColor Red
    }
}

function Launch-Application {
    if (Test-Path $localJarPath) {
        Write-Host "Lancement de l'application..." -ForegroundColor Cyan
        Start-Process -NoNewWindow -Wait -FilePath $javaCommand -ArgumentList "$javaOptions `"$localJarPath`""
    } else {
        Write-Host "Erreur : Le fichier JAR de l'application est introuvable." -ForegroundColor Red
    }
}

# Script principal
Write-Host "Vérification des mises à jour..." -ForegroundColor Cyan
$localVersion = Get-LocalVersion
$remoteVersion = Get-RemoteVersion

if ($remoteVersion -and ($remoteVersion -ne $localVersion)) {
    Write-Host "Une nouvelle version est disponible : $remoteVersion (Actuelle : $localVersion)" -ForegroundColor Yellow
    Update-Application
} elseif ($remoteVersion -eq $localVersion) {
    Write-Host "L'application est à jour (Version : $localVersion)." -ForegroundColor Green
} else {
    Write-Host "Impossible de vérifier la version. Lancement de l'application actuelle." -ForegroundColor Yellow
}

# Lancer l'application
Launch-Application
