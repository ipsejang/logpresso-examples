// # Create/Read/Delete 예제

// 이 로그프레소 앱은 생성(Create)/읽기(Read)/삭제(Delete) 예제를 보여주는 앱입니다.

// 이 예제 앱을 통해 앱에서 많이 쓰이는 패턴인 생성/읽기/삭제 과정을 UI 상에서 어떻게 구현할 수 있는지 알 수 있습니다.

// 이 예제에서는 SQL로부터 하나의 열을 생성/읽기/삭제 할 수 있음을 보여줍니다. 실제 JDBC 프로파일을 이용해서 SQL과의 통신을 구현하는 부분은 Java 파일에서 찾을 수 있으며,
// 실질적으로 생성/읽기/삭제 등은 메시지버스 명령을 이용하여 접근합니다.

// 메시지버스 명령은 `socket` 서비스의 `send` 메소드를 통해 실행할 수 있으며, `DemoAppPlugin` 아래의 다양한 명령을 실행하는 것을 이 앱에서 확인할 수 있습니다.

// ### 사전 준비물
// * mariadb 혹은 호환 DB
// * JDBC 프로파일
//  - demo	jdbc:mysql://localhost:3306/app_tutorial	root	false
// * 다음 쿼리를 실행하여 DB 스키마를 만듭니다.
// 		dbquery ..

// ### 앱의 시작
// 앱은 괄호로 닫혀진 [즉시 실행하는 anonymous function](http://markdalgleish.com/2011/03/self-executing-anonymous-functions/) 의 형태로 만들어져야 합니다.
(function() {

	// ### 고유 아이디 부여
	// 앱이 가지는 고유 아이디를 받아옵니다.
	var pid = LOGPRESSO.process.getPid();

	// ### 메인 컨트롤러 작성
	// `SampleCRUDController`는 이 앱의 메인 컨트롤러 이름입니다. angular.js의 컨트롤러의 형태로 작성되어져야 하며, 기본적으로 `$scope`를 주입해야 합니다.
	// 또한 로그프레소 메시지버스를 이용하려면 `socket`이라는 내장 서비스를 주입해야 합니다.
	function SampleCRUDController($scope, socket, $element) {

		$scope.errorMessage = '';
		$scope.dataSource = [];
		$scope.insertData = {};

		// ### 목록 읽기
		// DB에 저장되어있는 목록을 불러옵니다.
		$scope.getServices = function() {
			$scope.dataSource = [];
			// `socket.send(command, options, pid)` 형태로 메시지버스를 호출하며, 성공 및 실패시의 행동을 Promise 형태로 등록합니다.

			// DemoAppPlugin의 getService 명령은 옵션은 필요로 하지 않으므로, 빈 객체 리터럴을 넘겨줍니다.
			socket.send('com.logpresso.example.app.DemoAppPlugin.getServices', {}, pid)
			.success(function(m) {
				var services = m.body.services;
				services.forEach(function(obj) {
					$scope.dataSource.push(obj);
				});

				$scope.$apply();
			})
			.failed(msgbusFail);
		}

		// ### 새로운 열 생성
		$scope.insertNew = function() {
			// DemoAppPlugin의 insertService 명령을 통해 새 데이터를 삽입할 때는, HTML과의 Two-way 바인딩으로 만들어진 `$scope.insertData`를 복사해서 넘겨줍니다.

			// ```html
			// <label>
			// 	{{'$K_CRUD.$S_Name' | translate}}
			// 	<input type="text" ng-model="insertData.code" required/>
			// </label>
			// <label>
			// 	{{'$K_CRUD.$S_Comments' | translate}}
			// 	<input type="text" ng-model="insertData.name" required/>
			// </label>
			// ```
			//
			// 바인딩하는 HTML 코드는 위와 같으며, `ng-model`을 통해 미루어볼 때, 옵션에 들어가는 데이터는 `{ "code": "some text", "name": "example text" }` 와 같은 형태가 됨을 알 수 있습니다.
			var data = angular.copy($scope.insertData);
			socket.send('com.logpresso.example.app.DemoAppPlugin.insertService', data, pid)
			.success(function(m) {
				$scope.$apply(function() {
					data.id = m.body.id;
					$scope.dataSource.splice(0, 0, data);
				});
			})
			.failed(msgbusFail);

			$scope.insertData = {};
		}

		// ### 기존의 열 삭제
		$scope.removeServices = function(data) {
			// DemoAppPlugin의 deleteServices 명령을 통해 열을 삭제할 때는, 삭제하려는 대상 열의 id를 배열 형태로 넘겨주면 됩니다.

			// UI에서는 단순히 하나의 열만 삭제 할 수 있게 구현되어 있으므로, `[data.id]` 와 같이 대상 id를 배열로 감싸줍니다.
			var idx = $scope.dataSource.indexOf(data);
			socket.send('com.logpresso.example.app.DemoAppPlugin.deleteServices', {'services': [data.id]}, pid)
			.success(function(m) {
				$scope.$apply(function() {
					$scope.dataSource.splice(idx, 1);
				});
			})
			.failed(msgbusFail);
		}

		// ### 에러 처리
		// 메시지버스 명령에 실패할 경우에 모달창을 띄워 에러를 표시합니다.
		function msgbusFail(m, raw) {
			// 모달창을 띄우려면, `$element` 서비스를 이용해 모달창을 찾고, element에 내장되어있는 `showDialog()` 메소드를 실행합니다.
			$element.find('.mdlError')[0].showDialog();
			$scope.errorMessage = raw;
			$scope.$apply();
		}

		// ### 앱 초기화
		// 컨트롤러가 활성화되는 시점에 앱이 필요로 하는 초기화 코드를 넣어줍니다.
		$scope.getServices();
	}

	// ### 메인 컨트롤러 등록
	// 컨트롤러를 등록해서 로그프레소가 이 앱의 컨트롤러를 인식할 수 있도록 합니다.
	app.register.controller('SampleCRUDController', SampleCRUDController);
})();

//# sourceURL=apps/app-tutorial/crud/app.js