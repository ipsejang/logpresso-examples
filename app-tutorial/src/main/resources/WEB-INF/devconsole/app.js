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

	extension.global.addController(DevConsoleController);
})();