(function() {

	function DashboardAssetWordCloudController($scope, socket) {
		
	}

	function DashboardAssetWordCloudPropertyController($scope, $element, $timeout) {
		$timeout(function() {

			var comp = $element[0];
			comp.addValidator(function(val) {
				var valid = true;
				if(!val.size) {
					valid = false;
				}
				if(!val.text) {
					valid = false;
				}
				return valid;
			});

			comp.addFormatter(function(val) {
				return val;
			});

			$scope.output = {
				size: undefined,
				text: undefined				
			}

		});
	}

	extension.global.addController(DashboardAssetWordCloudController);
	extension.global.addController(DashboardAssetWordCloudPropertyController);
	
})();