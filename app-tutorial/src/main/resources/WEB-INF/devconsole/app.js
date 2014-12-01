// # 개발자 콘솔 예제

// 이 로그프레소 앱은 메시지버스를 이용하는 방법을 보여줍니다.

// 로그프레소 메시지버스란, 로그프레소 코어가 웹콘솔 등과 통신하는 채널을 말하는 것으로 WebSocket 기술을 사용하며, UI에서는 로그프레소에 내장되어 있는 WebSocket 서버와 통신할 수 있도록 만들어져 있습니다.

// 메시지버스 명령은 `socket` 서비스의 `send` 메소드를 통해 실행할 수 있으며,
// 이 앱에서는 로그프레소 CLI의 `msgbus.plugin` 명령을 통해 확인할 수 있는 다양한 메시지버스 명령을, UI에 준비된 입력 상자를 통해 실행할 수 있게끔 만들어져 있습니다.

// ### 앱의 시작
// 앱은 괄호로 닫혀진 [즉시 실행하는 anonymous function](http://markdalgleish.com/2011/03/self-executing-anonymous-functions/) 의 형태로 만들어져야 합니다.
(function() {
	var pid = LOGPRESSO.process.getPid();

	function DevConsoleController($scope, socket) {
		var INIT_MSGBUS = 'org.araqne.logdb.msgbus.ManagementPlugin.listTables';
		var INIT_OPTION = '{}';
		$scope.modelMsgbus = INIT_MSGBUS;
		$scope.modelOption = INIT_OPTION;
		$scope.isOnSubmit = false;

		$scope.keyevent = function(e) {
			if ((e.ctrlKey || e.shiftKey) && e.keyCode === 13) {
				e.preventDefault();
				$scope.run();
			}
		}

		$scope.run = function() {
			$scope.isOnSubmit = true;
			var option;
			try {
				option = JSON.parse($scope.modelOption);
			}
			catch(e) {
				$scope.frm.options.$invalid = true;
				return;
			}

			$scope.frm.options.$invalid = false;
			socket.send($scope.modelMsgbus, option, pid)
			.success(function(m) {
				console.log(m);
				$scope.$apply(function() {
					$scope.isOnSubmit = false;
					$scope.resultSuccess = m.body;
				});
			})
			.failed(function(m, raw) {
				console.log(m);
				console.log(raw);
				$scope.$apply(function() {
					$scope.isOnSubmit = false;
					$scope.resultFailed = raw;
				});
			})
		}

		$scope.clear = function() {
			$scope.frm.$setPristine();
			$scope.isOnSubmit = false;
			$scope.modelMsgbus = INIT_MSGBUS;
			$scope.modelOption = INIT_OPTION
			$scope.resultSuccess = undefined;
			$scope.resultFailed = undefined;
		}
	}

	app.register.controller('DevConsoleController', DevConsoleController);
})();