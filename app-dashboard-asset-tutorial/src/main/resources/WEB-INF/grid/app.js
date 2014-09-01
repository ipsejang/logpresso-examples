(function() {

	function DashboardAssetGridController($scope, socket) {
		
	}

	function DashboardAssetGridPropertyController($scope, $element, $timeout) {
		$timeout(function() {

			var comp = $element[0];
			comp.addValidator(function(val) {
				var valid = true;
				return valid;
			});

			comp.addFormatter(function(val) {
				var ret = val.filter(function(c) {
					return c.is_checked;
				})
				.map(function(c) {
					return c.key;
				});
				return {
					order: ret
				}
			});

			$scope.columnInfo.forEach(function(c) {
				c.is_checked = true;
			});

			$scope.orderingEnabled = true;

			$scope.sortableOptions = {
				axis: 'y',
				distance: 5
			};
		});
	}

	extension.global.addController(DashboardAssetGridController);
	extension.global.addController(DashboardAssetGridPropertyController);
	
})();