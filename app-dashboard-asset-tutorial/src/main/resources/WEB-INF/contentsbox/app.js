(function() {

	function DashboardAssetContentsBoxService($rootScope, $compile) {

		function render(targetEl, json, ctx){
			var scope = $rootScope.$new();
			scope.ctx = ctx;

			scope.contents = json[ctx.row - 1][ctx.column];

			var el = angular.element('<div class="widget" style="padding:20px; box-sizing:border-box"><div>내용: <strong style="font-size: 2em">{{contents}}</strong></div> <div>위젯 컨텍스트:</div><pre>{{ctx | json}}</pre></div>');
			$compile(el)(scope);
			el.appendTo(targetEl);
			if(!scope.$$phase) {
				scope.$digest();
			}

			return {
				update: function(data) {
					scope.$digest();
				}
			}
		}

		return {
			render: render
		}
	}

	function DashboardAssetContentsBoxPropertyController($scope, $element, $timeout) {
		$timeout(function() {

			var comp = $element[0];
			comp.addValidator(function(val) {
				var valid = true;
				if(!val.column) {
					valid = false;
				}
				return valid;
			});

			comp.addFormatter(function(val) {
				return val;
			});

			$scope.output = {
				column: undefined,
				row: 1
			}

		});
	}

	extension.asset.addType('contentsbox', DashboardAssetContentsBoxService);
	extension.global.addController(DashboardAssetContentsBoxPropertyController);
	
})();