(function() {
	/**
	 * 사용자 정의 위젯 개발에는 두가지 함수가 필요합니다.
	 *
	 * 첫번째는 서비스 함수로, 위젯의 렌더링을 담당하는 함수입니다. angular.js의 서비스의 형태로 호출됩니다.
	 * 또 다른 하나는, 대시보드 프로그램 상에서 위젯을 만들때 쓰이는 위젯 속성 편집 창에 필요한 함수입니다.
	 * 이는 angular.js의 컨트롤러의 형태로 사용합니다.
	 */

	/**
	 * @module service
	 * @name serviceAssetContentsBox
	 * @description
	 * ContentsBox 서비스 함수입니다.
	 * 파라미터에 로그프레소에서 사용 가능한 서비스(여기서는 $rootScope, $compile)들을 추가해서 이용할 수 있습니다.
	 *
	 * angular.js 의 서비스에 대한 설명은 [Creating Services](https://docs.angularjs.org/guide/services) 에서 찾을 수 있습니다.
	 * 서비스에는 $scope를 사용할 수 없습니다.
	 */
	function DashboardAssetContentsBoxService($rootScope, $compile) {

		/**
		 * @function
		 * @name render
		 *
		 * @description
		 * 위젯을 렌더링하는 함수입니다.
		 * 모든 위젯에는 반드시 있어야 하는 함수로, 서비스에서 반드시 render라는 이름으로 리턴해야 합니다.
		 *
		 * @param {jQueryWrappedElement} targetEl 위젯이 삽입될 element 입니다. 타입은 jQuery 로 감싸진 형태입니다.
		 * @param {JSON} json 쿼리 결과를 담고 있는 JSON 입니다.
		 * @param {JSON} ctx 위젯의 컨텍스트를 담고 있는 JSON 입니다.
		 */
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
				/**
				 * @function
				 * @name update
				 *
				 * @description
				 * 위젯을 업데이트하는 함수입니다.
				 * render 함수는 반드시 update라는 이름으로 리턴해야 합니다.
				 *
				 * @param {JSON} json 쿼리 결과를 담고 있는 JSON 입니다.
				 */
				update: function(data) {
					scope.$digest();
				}
			}
		}

		return {
			render: render
		}
	}

	/**
	 * @constructs DashboardAssetContentsBoxPropertyController
	 * @name DashboardAssetContentsBoxPropertyController
	 * @description
	 * ContentsBox 속성 편집창에 필요한 컨트롤러입니다.
	 * property.html 에서 `ng-controller="DashboardAssetContentsBoxPropertyController"` 의 형태로 쓸 수 있습니다.
	 *
	 * angular.js 의 컨트롤러에 대한 설명은 [Understanding Controllers](https://docs.angularjs.org/guide/controller) 에서 찾을 수 있습니다.
	 */
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

	// 위젯 서비스를 등록합니다.
	extension.asset.addType('contentsbox', DashboardAssetContentsBoxService);

	// 로그프레소 앱에서 사용할 컨트롤러를 등록합니다.
	extension.global.addController(DashboardAssetContentsBoxPropertyController);
	
})();