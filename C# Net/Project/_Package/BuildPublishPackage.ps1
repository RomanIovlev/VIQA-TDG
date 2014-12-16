$version = $args[0]
$target = $args[1]
$conf = $args[2]
$publish = $args[3]

$libs = @()
$packageId = $target

$apiKey = "48e9dc2f-3575-4b10-a49f-9c850fd40537"
$src = "https://nuget.pl3.com"

function getPackageVersion($pack) {
    $str = iex 'nuget list "$pack" -source $src/api/v2'
    $verCheck = ([regex]"$pack (\d+[\d\.]+\d+)").match($str)
    $ver = $null
    if($verCheck.Success) {
        $ver = $verCheck.Groups[1].Value
    }
    return New-Object PSObject -Property @{ exists=$verCheck.success; ver=$ver; }
}

function compareVersions($ver1, $ver2) {
    if ($ver1 -eq $ver2) {
        return 0
    }
    $ver1s = $ver1.split(".")
    $ver2s = $ver2.split(".")
    if ($ver1.length -ne $ver2.length) {
        Write-Host Bad versions to compare: $ver1 - $ver2
    }
    for ($i = 0; $i -le $ver1.length; $i++) {
        $v1part = $ver1s[$i] -as [int]
        $v2part = $ver2s[$i] -as [int]
        if ($v1part -gt $v2part) {
            return 1
        }
        if ($v1part -lt $v2part) {
            return -1
        }
    }
    return 0
}

xcopy /C/H/R/Y/B/S _Package\content\*.* ..\~Redistr\$conf\content\*.*
xcopy /C/H/R/Y/B _Package\tools\*.* ..\~Redistr\$conf\tools\*.*
xcopy /C/H/R/Y/B bin\$conf\$target.dll ..\~Redistr\$conf\lib\net35\*.*
xcopy /C/H/R/Y/B bin\$conf\$target.pdb ..\~Redistr\$conf\lib\net35\*.*
foreach ($lib in $libs) {
    xcopy /C/H/R/Y/B bin\$conf\$($lib).dll ..\~Redistr\$conf\lib\net35\*.*
    xcopy /C/H/R/Y/B bin\$conf\$($lib).pdb ..\~Redistr\$conf\lib\net35\*.*
}
xcopy /C/H/R/Y/B _Package\Package.nuspec ..\~Redistr\$conf\*.*

cd ..\~Redistr\$conf\

Write-Host
Write-Host Pack...
iex "nuget pack Package.nuspec -Verbosity Normal -Version $version -Properties Configuration=$conf"

Write-Host
Write-Host Clean...
if (Test-Path content) {
    rm content -Recurse -Force -Confirm:$false
}
if (Test-Path tools) {
    rm tools -Recurse -Force -Confirm:$false
}
if (Test-Path lib) {
    rm lib -Recurse -Force -Confirm:$false
}
if (Test-Path Package.nuspec) {
    rm Package.nuspec -Recurse -Force -Confirm:$false
}

# Check if package should be published
if ($publish -eq "Publish") {
    Write-Host
    Write-Host List package $packageId ...
    $ver = getPackageVersion $packageId
    Write-Host Remote package: $ver
    if ($ver.exists) {
        $comp = compareVersions $ver.ver $version
        if ($comp -lt 0) {
            Write-Host Publishing package...
            iex "nuget push $($packageId).$($version).nupkg $apiKey -source $src"
        } else {
            Write-Host "Package $($packageId).$($version).nupkg already published with version $($ver.ver)"
        }
    } else {
        Write-Host "Could not publish: package $($packageId).$($version).nupkg not found"; Exit 2
    }
}
