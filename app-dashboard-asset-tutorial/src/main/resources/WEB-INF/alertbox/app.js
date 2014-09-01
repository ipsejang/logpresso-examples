(function() {

	function DashboardAssetAlertBoxController($scope, socket) {
		
	}

	function DashboardAssetAlertBoxPropertyController($scope, $element, $timeout) {
		$timeout(function() {

			var comp = $element[0];
			comp.addValidator(function(val) {
				var valid = true;
				if(!val.column) {
					valid = false;
				}
				if(!val.default_color) {
					valid = false;
				}
				if(val.rules.length == 0) {
					valid = false;
				}
				return valid;
			});

			comp.addFormatter(function(val) {
				return val;
			});

			$scope.output = {
				rules: [{
					boundary: 0,
					operator: '>',
					color: color_map[0]
				}],
				default_color: '#eeeeee',
				formatting: ''
			}

			var REX_COMMA = /,/g;
			var REX_DECIMAL = /.\df/g;

			$scope.comma = false;
			$scope.$watch('comma', function(val, old) {
				if(val == undefined) return;
				if(val == old) return;

				$scope.output.formatting = $scope.output.formatting.replace(REX_COMMA, '');
				if(val) {
					$scope.output.formatting = ',' + $scope.output.formatting;
				}
			});

			$scope.decimal = 0;
			$scope.$watch('decimal', function(val, old) {
				if(val == undefined) return;
				if(val == old) return;

				
				$scope.output.formatting = $scope.output.formatting.replace(REX_DECIMAL, '');	
				if(val > 0) {
					$scope.output.formatting = $scope.output.formatting + '.' + val + 'f';
				}
			});

			$scope.addRule = function() {
				$scope.output.rules.push({
					boundary: null,
					operator: '<',
					color: color_map[$scope.output.rules.length]
				});
			}

			$scope.removeFrom = function(idx, src) {
				src.splice(idx, 1);
			}

		});
	}

	extension.global.addController(DashboardAssetAlertBoxController);
	extension.global.addController(DashboardAssetAlertBoxPropertyController);
	
})();