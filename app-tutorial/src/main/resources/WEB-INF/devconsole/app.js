(function() {

	function DevConsoleController($scope, socket) {
		$scope.modelMsgbus = "org.araqne.logdb.msgbus.ManagementPlugin.listTables";
		$scope.modelOption = "{}";

		$scope.run = function() {
			socket.send($scope.modelMsgbus, JSON.parse($scope.modelOption), 49494959)
			.success(function(m) {
				console.log('----------success----------');
				console.log(m);
				
				$scope.$apply(function() {
					$scope.resultSuccess = m.body;
				});
			})
			.failed(function(m, raw) {
				console.log('----------failed----------');
				console.log(m);
				console.log(raw);

				$scope.$apply(function() {
					$scope.resultFailed = raw;
				});
			})
		}

		$scope.clear = function() {
			$scope.modelMsgbus = "";
			$scope.modelOption = "";
			$scope.resultSuccess = "";
			$scope.resultFailed = "";
		}

	}

	extension.global.addController(DevConsoleController);

	
})();