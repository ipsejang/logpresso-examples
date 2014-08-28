(function() {

	function SampleLogdbServiceController($scope, socket, serviceLogdb) {

		console.log('-------------- SampleLogdbServiceController -----------------')
		var instance = serviceLogdb.create(12341234);

		console.clear();
		console.log(instance);

		var q = instance.query('table logpresso_query_logs', 100)

		console.log(q);

		q.created(function(m) {
			console.log('created', m);
		})
		.started(function(m) {
			console.log('started', m);
		})
		.onStatusChange(function(m) {
			console.log('onStatusChange', m);
		})
		.onHead(function(helper) {
			console.log('onHead', helper);
		})
		.onTail(function(helper) {
			console.log('onTail', helper);

			helper.getResult(function(m) {
				console.log('getResult', m);
			});


		})
		.loaded(function(m) {
			console.log('loaded', m);
		})
		.failed(function(m, raw) {
			console.log('failed', m, raw);
		});


	}

	function SampleLogdbDirectiveController($scope, socket, serviceLogdb) {
		$scope.queryString = '';
		$scope.queryResult;
		$scope.numPageSize = 20;
		
		$scope.numTotalCount = 0;
		$scope.numPageSize = 50;
		$scope.numPagerPageSize = 10;
		$scope.numCurrentPage = 0;

		$scope.changePage = function (idx) {
			$scope.numCurrentPage = idx;
			angular.element('#demo-qi-1')[0].getInstance().getResult(idx * $scope.numPageSize, $scope.numPageSize, function(m) {
				$scope.$apply(function() {
					$scope.modelTable = m.body.result;
				});
			});
		}

		$scope.numPid = 123;

		$scope.onHead = function(helper) {
			console.log('onHead', helper)
			helper.getResult(function(m) {
				console.log(m);

				if($scope.optionResultCursor === 'head') {
					$scope.modelTable = m.body.result;
					$scope.$apply();
				}
			});
		}

		$scope.onTail = function(helper) {
			console.log('onTail', helper)
			$scope.numTotalCount = helper.message.body.total_count;
			// helper.getResult(function(m) {
			// 	console.log(m);

			// 	if($scope.optionResultCursor === 'tail') {
			// 		$scope.modelTable = m.body.result;
			// 		$scope.$apply();
			// 	}
			// });
$scope.$apply();
		}

		$scope.optionResultCursor = 'head';

		$scope.modelTable = [];

	}

	function SampleLogdbController($scope, socket) {
		$scope.hello = 'world';

	}

	extension.global.addController(SampleLogdbServiceController);
	extension.global.addController(SampleLogdbDirectiveController);
	extension.global.addController(SampleLogdbController);
	
})();