### 빌드

- eclipse에서 maven project 로 import 합니다
- maven build 를 실행합니다.

### 설치

- logpresso CLI 에서 `bundle.install` 명령을 통해 빌드된 `app-tutorial-1.0.0.jar` 파일을 설치합니다.
- `bundle.refresh` 명령으로 새로고침합니다.
- `apptutorial.install` 명령으로 프로그램 및 프로파일에 등록합니다.

# 앱 제작

### 디렉토리 및 파일 구성

- src/main/java
	- com.logpresso.example.app
		- DemoAppInstaller.java
		- DemoAppPlugin.java
		- DemoAppProvider.java
	- com.logpresso.example.app.script
		- DemoAppScript.java
		- DemoAppScriptFactory.java
- src/main/resources
	- WEB-INF
		- program_id
			- locales
				- en.json
				- ko.json
			- app.html
			- app.js
			- config.js
			- index.html

### 각 파일에 대한 설명

##### 1. Manifest 정의

- DemoAppProvier.java
	- 앱 및 앱에 속한 프로그램들의 고유 아이디를 정의합니다.
	- 앱이 실행 및 중지될 때 서블릿을 등록 또는 해제합니다.

##### 2. 인스톨러

- DemoAppInstaller.java
	- 앱을 프로그램 팩으로, 앱에 속한 프로그램을 프로그램으로 등록합니다.
	- 그 후에 권한에 따라 앱을 등록합니다.

- DemoAppScript.java
- DemoAppScriptFactory.java
	- 로그프레소 CLI 에서 `apptutorial.install` 명령으로 인스톨러를 실행할 수 있도록 도와줍니다.

##### 3. 서버 로직

- DemoAppPlugin.java
	- 앱 내에서 필요한 서버 로직을 작성하고, 필요에 따라 메시지버스를 통해 실행할 수 있게 메시버스 메소드로 등록합니다. (예제에서는 SQL을 통해 단순 CRUD 작업을 처리하고 있으며, CRUD 앱에서 메시지버스를 통해 사용하고 있는 것을 확인할 수 있습니다.)

##### 4. UI 개발

- app.html
	- 앱을 로딩하는데 필요한 컨테이너입니다. 서드 파티 라이브러리를 사용하는 경우, 이 파일을 통해 등록할 수 있습니다. 별다른 필요가 없는 경우 기본값대로 쓰면 됩니다.
- config.js
	- 앱의 URL 라우팅 정보 및 로케일 정보를 정의하는 설정 파일입니다.
- index.html
	- 실질적으로 앱의 UI를 표시하는 마크업입니다. 이 안에 `script`태그를 이용해 UI 로직을 추가적으로 로딩할 수 있습니다.

### 앱 라우팅 정보 설정 방법

##### 개요
앱 내에 다음과 같은 디렉토리 구조가 있다고 가정해봅시다.

- src/main/resources
	- WEB-INF
		- program_id
			- locales
				- en.json
				- ko.json
			- users
				- users.js
				- index.html
			- app.html
			- app.js
			- config.js
			- index.html

앱 내 메뉴 구성이 다음과 같을 때, URL 라우팅을 따로 정의 할 수 있습니다.
다음은 각 URL에 따른 샘플 화면 예제입니다.

##### 1. 첫 페이지
http://logpresso/#/app_id/program_id
```

	[ -> 첫 페이지 ] [ 사용자 목록 ]
	--------------------------
		여기는 첫 페이지입니다.

```

##### 2. 사용자 목록 페이지
http://logpresso/#/app_id/program_id/users
```

	[ 첫 페이지 ] [ -> 사용자 목록 ]
	--------------------------
		사용자 목록
		- mindori
		- babo
		- xeraph


```

##### 3. 특정 사용자 페이지
http://logpresso/#/app_id/program_id/users/mindori

```

	[ 첫 페이지 ] [ -> 사용자 목록 ]
	------------------------------
		사용자 목록 - mindori
		mindori 님의 페이지입니다.

````

##### 라우팅 설정
위와 같은 화면 구성에 따른 라우팅 설정을 하면 다음과 같습니다.

```javascript
app.config(function($stateProvider, $urlRouterProvider, serviceUtility) {

	var path = { path: '^/app_id/program_id' };
	var absPath = {
		absPath: '/apps/app_id/program_id',
		idxPage: 'index.html'
	};


	$stateProvider
	.state('', { // 첫 페이지
		url: simpleTemplate.replace('{path}', path),
		views: {
			'': {
				templateUrl: replace('{absPath}/{idxPage}', absPath)
			}
		}
	})
	.state('users', { // 사용자 목록 페이지
		url: simpleTemplate.replace('{path}/users/', path),
		views: {
			'': {
				templateUrl: replace('{absPath}/users/{idxPage}', absPath)
			}
		}
	})
	.state('users.id', { // 특정 사용자 페이지
		url: simpleTemplate.replace('{path}/users/:id', path),
		views: {
			'': {
				templateUrl: replace('{absPath}/users/{idxPage}', absPath)
			}
		}
	});
});
```