	(function() {
	var pid = 12344321;

	function msgbusFail(m, raw) {
		console.log(m)
	}

	function SampleCRUDController($scope, $filter, socket, $translate) {
		$scope.dataSource = [];
		$scope.insertData = {};
		
		$scope.getServices = function() {
			$scope.dataSource = [];
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

		$scope.insertNew = function() {
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

		$scope.removeServices = function(data) {
			var idx = $scope.dataSource.indexOf(data);
			socket.send('com.logpresso.example.app.DemoAppPlugin.deleteServices', {'services': [data.id]}, pid)
			.success(function(m) {
				$scope.$apply(function() {
					$scope.dataSource.splice(idx, 1);
				});
				
			})
			.failed(msgbusFail);
		}

		$scope.getServices();
	}


	extension.global.addController(SampleCRUDController);
})();