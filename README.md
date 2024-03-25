<center>
    <img src="https://capsule-render.vercel.app/api?type=rounded&height=300&color=gradient&text=BoothListManager-nl-For%20Android">
</center>

# BoothListManager For Android

<a href="https://docs.google.com/spreadsheets/d/1TmZxEkJW17d0I1MmfNyzIIxjh1n_en1DKrwsbk2OzjM/edit#gid=0">
    <img alt="Static Badge" src="https://img.shields.io/badge/%ED%98%84%EC%9E%AC%20%EC%A0%81%EC%9A%A9%20%EC%A4%91%EC%9D%B8%20%EB%8F%99%EC%9D%B8%20%ED%96%89%EC%82%AC-%EC%A0%9C%204%ED%9A%8C%20%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%83%80%20%ED%8E%98%EC%8A%A4-yellow">
</a>

[BoothListManager][BoothListManager]를 기반으로 한 안드로이드용 앱

## 기능

[BoothListManager][BoothListManager] 에서 지원하는 기능을 안드로이드로 옮겨두었습니다.
단, 아직 부스 검색 기능은 개발 중입니다.
<br/><br/>

## 종속성
<table>
    <head>
        <th style="text-align: center">이름</th>
        <th style="text-align: center">설명</th>
    </head>
    <tbody>
        <tr>
            <td>com.chaquo.python</td>
            <td>코틀린에서 파이썬 모듈을 사용하기 위한 라이브러리</td>
        </tr>
        <tr>
            <td>android.compose.material3</td>
            <td>최신 UI 요소들을 사용하기 위한 라이브러리</td>
        </tr>
        <tr>
            <td>org.jetbrains.kotlinx:kotlinx-coroutines</td>
            <td>백그라운드 작업을 위한 코루틴 라이브러리</td>
        </tr>
    </tbody>
</table>
<br/>

## Structure
각 부분의 어떤 식으로 이루어져 있는지 설명합니다. 
### UI
<table>
    <head>
        <th style="text-align: center">페이지</th>
        <th style="text-align: center" width="50">코드</th>
        <th style="text-align: center">설명</th>
    </head>
    <tbody>
        <tr>
            <td style="text-align: center" rowspan="2">Home</td>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/home/HomeFragment.kt">Fragment</a></td>
            <td rowspan="2">현재 앱이 Google API에 연결되어 로그인되어 있는지 등의 정보를 표시하는 페이지</td>
        </tr>
        <tr>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/home/HomeViewModel.kt">ViewModel</a></td>
        </tr>
        <tr>
            <td style="text-align: center" rowspan="2">AddBooth</td>
            <td><a href="https://github.com/MinePacu/   BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/Booth/AddBoothFragment.kt">Fragment</a></td>
            <td rowspan="2">부스 정보 (부스 번호, 부스 이름, 장르, 인포, 선입금 링크 등)를 추가하는 페이지
        </tr>
        <tr>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/Booth/AddBoothViewModel.kt">ViewModel</a></td>
        </tr>
        <tr>
            <td style="text-align: center" rowspan="2">SearchBooth</td>
            <td>Fragment</td>
            <td rowspan="2">부스를 검색하는 페이지<br/>(단, 현재 개발 중입니다. 개발자가 부스 목록을 관리하는 중일 경우 지연될 수 있습니다.)</td>
        </tr>
        <tr>
            <td>ViewModel</td>
        </tr>
        <tr>
            <td style="text-align: center" rowspan="2">LinkGenerator</td>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/HyperLinkGenerator/HyperLinkGeneratorFragment.kt">Fragment</a></td>
            <td rowspan="2">시트에서 다른 시트의 Cell, 또는 같은 시트의 Cell에 연결하기 위한 HyperLink 함수를 사용한 문자열을 생성하는 페이지
        </tr>
        <tr>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/HyperLinkGenerator/HyperLinkGeneratorViewModel.kt">ViewModel</a></td>
        </tr>
        <tr>
            <td style="text-align: center" rowspan="2">Settings</td>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/Settings/SettingsFragment.kt">Fragment</a></td>
            <td rowspan="2">설정 페이지. 시트 ID와 시트 인덱스 등 정보를 추가 또는 수정할 시트에 대한 정보를 설정하는 페이지</td>
        </tr>
        <tr>
            <td><a href="https://github.com/MinePacu/BoothListManager_ForAndroid/blob/master/app/src/main/java/com/minepacu/boothlistmanager/ui/Settings/SettingsViewModel.kt">ViewModel</a></td>
        </tr>
    </tbody>
</table>

### 기타 클래스
<table>
    <head>
        <th style="text-align: center">클래스</th>
        <th style="text-align: center">설명</th>
    </head>
    <tbody>
        <tr>
            <td>PythonClass</td>
            <td>코틀린에서 파이썬으로 코딩된 모듈을 이용할 수 있는 클래스. 대부분의 함수가 백그라운드에서 구동될 수 있도록 코루틴으로 구성되어 있습니다.</td>
        </tr>
    </tbody>
</table>

### 파이썬
<table>
    <head>
        <th style="text-align: center">모듈</th>
        <th style="text-align: center">설명</th>
    </head>
    <tbody>
        <tr>
            <td>BoothListManager</td>
            <td>Google API 로그인부터, 시트에 부스 정보 추가까지 가장 근본이 되는 코드들이 있는 파이썬 모듈</td>
        </tr>
    </tbody>
</table>


[//]: #

[BoothListManager]: <https://github.com/MinePacu/BoothListManager>
