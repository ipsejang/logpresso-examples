(function() {

	function DashboardAssetChartController($scope, socket) {
		
	}

	function DashboardAssetChartPropertyController($scope, $element, $timeout) {
		$timeout(function() {

			var comp = $element[0];
			comp.addValidator(function(val) {
				var valid = true;
				if(!val.autoSeries && val.series.length == 0) {
					valid = false;
				}

				if(!val.type) {
					valid = false;
				}

				if(!val.label) {
					valid = false;
				}

				if(!val.labelType) {
					valid = false;
				}

				return valid;
			});

			comp.addFormatter(function(val) {
				if(val.autoSeries) {
					delete val.series;
				}
				else {
					delete val.excludeSeries;
				}

				return val;
			});

			$scope.output = {
				autoSeries: false,
				excludeSeries: [],
				series: [
				{
					color: color_map[0]
				}
				]
			}

			$scope.indepVar;
			$scope.$watch('indepVar', function(val, old) {
				console.log(val, old)
				if(val == undefined) return;
				if(val == old) return;

				$scope.output.label = val.key;
				$scope.output.labelType = val.type;
			});

			$scope.addExcludeSeries = function() {
				$scope.output.excludeSeries.push({
					color: '#ffcc00'
				});
			}

			$scope.addSeries = function() {
				$scope.output.series.push({
					color: '#ffcc00'
				});
			}

			$scope.removeFrom = function(idx, src) {
				src.splice(idx, 1);
			}

			$scope.setName = function(series) {
				series.name = series.key;
			}
		});
	}

	extension.global.addController(DashboardAssetChartController);
	extension.global.addController(DashboardAssetChartPropertyController);
	
})();