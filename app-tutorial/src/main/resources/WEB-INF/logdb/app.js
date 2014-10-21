// # 쿼리 결과 페이징 예제

// 이 로그프레소 앱은 쿼리 및 결과에 대한 페이징 예제를 보여주는 앱입니다.

// 로그프레소는 쿼리 엔진을 통하여 로그스토리지에 적재된 로그들을 쿼리할 수 있습니다. 이 예제는 직접 쿼리문을 입력하여 조회하고, 그 결과를 일정 단위로 끊어서 페이징 할 수 있도록 제작되었습니다.
// 쿼리는 다음과 같은 과정으로 이루어집니다.

// <그림> create, started, push 등등의 과정 설명

// 로그프레소가 제공하는 API를 이용하여 각각의 과정, 즉 이벤트 발생에 따른 이벤트 핸들러를 등록할 수 있습니다.

// 이 과정에 대한 자세한 설명은 다음을 참조하십시오.

// 이 때 중요한 것은 반드시 쿼리 결과의 사용이 끝나면 삭제해줘야 한다는 것입니다. 그렇지 않다면 더이상 사용하지 않는 쿼리 결과가 메모리에 계속 남아있게 되어 성능에 지장을 줄 수 있습니다.

// 이 소스를 통해 쿼리 및 쿼리 결과를 조회하는 방법에 대해 알수 있습니다. 또한 쿼리 및 쿼리 결과 조회에 도움을 주는 UI 컨트롤들을 소개하고, 이를 이용하는 법을 알아봅니다.

// ### UI 컨트롤 소개
// index.html 파일을 보면 `<query-input>`, `<table-view-with-pager>` 등의 directive 를 볼 수 있습니다.

// 이는 로그프레소가 제공하는 UI 컨트롤의 일부로, 쿼리문을 입력해서 쿼리하고, 그 결과를 테이블 형태로 페이징해서 보여줄 수 있는 기능들을 제공하고 있습니다.

// #### &lt;query-input&gt; 컨트롤

// `<query-input>`컨트롤은 쿼리 입력창과 검색(쿼리) 버튼, 그리고 쿼리 도중 쿼리를 중단할 수 있는 버튼을 제공하고 있습니다.

// ```html
// <query-input
// 	id="demo-qi-1"
// 	ng-model="queryResult"
// 	ng-query-string="queryString"
// 	ng-page-size="numPageSize"
// 	ng-pid="numPid"
// 	on-head="onHead($helper)"
// 	on-status-change="onStatusChange($msg)"
// 	on-tail="onTail($helper)"
// 	on-failed="onFailed($raw, $type, $note)">
// </query-input>
// ```

// 위 속성 중 반드시 필요한 것은 `ng-model`, `ng-query-string`, `ng-page-size`, `ng-pid`로, 이에 대한 설명은 다음과 같습니다.
// * `ng-model` 쿼리 결과를 적재할 모델입니다.
// * `ng-query-string` 쿼리문을 바인딩할 모델입니다.
// * `ng-page-size` 수백, 수천만개의 쿼리 결과들을 일정 단위로 끊어서 받아올 수 있도록 페이징 크기를 설정하는 모델입니다.
// * `ng-pid` 앱의 고유 아이디입니다.

// 기타 속성 및 메소드들에 대한 자세한 설명은 API 문서를 참조하십시오.

// #### &lt;table-view-with-pager&gt; 컨트롤

// `<table-view-with-pager>`컨트롤은 단순히 테이블과 페이저를 합쳐놓은 컨트롤로, 배열로 된 결과물을 페이징해서 보여줄 수 있도록 제작된 컨트롤입니다. 여기에 이벤트 핸들러를 등록해 페이지가 변경될 때마다 표시될 모델을 변경하는 과정을 코딩합니다.

// ```html
// <table-view-with-pager
// 	ng-model="queryResult"
// 	ng-total-count="numTotalCount"
// 	ng-items-per-page="numPageSize"
// 	ng-page-size="numPagerPageSize"
// 	on-page-change="changePage($idx)">
// </table-view-with-pager>
// ```

// * `ng-model` 테이블에 표시할 (페이징된) 모델입니다.
// * `ng-total-count` 페이징해서 보여줄 전체 모델의 총 row의 갯수를 바인딩합니다.
// * `ng-items-per-page` 한 페이지에 몇개의 row를 표시할 지를 결정합니다.
// * `ng-page-size` 페이저 자체에 몇개의 인덱스를 표시할 지를 결정합니다.
// * `on-page-change` 페이지가 변경될 때마다 실행할 이벤트 핸들러입니다. `$idx`로 인덱스를 넘겨줄 수 있습니다.

// 마찬가지로 기타 속성 및 메소드들에 대한 자세한 설명은 API 문서를 참조하십시오.

// 이제 자바스크립트 코드를 통해 이러한 컨트롤을 어떻게 사용하는지 알아봅니다.

// ### 앱의 시작
(function() {
	// ### 고유 아이디 부여
	// 앱이 가지는 고유 아이디를 받아옵니다.
	var pid = LOGPRESSO.process.getPid();

	// ### 메인 컨트롤러 작성
	// `SampleLogdbDirectiveController`는 이 앱의 메인 컨트롤러 이름입니다. angular.js의 컨트롤러의 형태로 작성되어져야 하며, 기본적으로 `$scope`를 주입해야 합니다.

	// 또한 로그DB 및 쿼리와 관련된 서비스를 이용하려면 `serviceLogdb`라는 로그프레소 내장 서비스를 주입합니다.

	// `eventSender`는 컨트롤러간의 메소드 전달을 위한 일종의 창구입니다. 이에 대한 자세한 설명은 문서를 참조하십시오.
	// 여기에서는 앱의 종료 이벤트를 등록하기 위한 목적으로 주입했습니다.
	function SampleLogdbDirectiveController($scope, serviceLogdb, eventSender) {

		// ### 모델 정의

		// `<query-input>`에서 쓰이는 모델들입니다.
		$scope.numPid = pid;
		$scope.queryString = '';
		$scope.queryResult = [];

		// `<query-input>`에서의 페이징 크기는 `<table-view-with-pager>`에서 한 페이지에 보여질 row 갯수와 같아야(혹은 커야) 한 번의 쿼리에 한 페이지를 가득 채울 수 있습니다.
		$scope.numPageSize = 20;

		// `<table-view-with-pager>`에서 쓰이는 모델들입니다.
		$scope.numTotalCount = 0;
		$scope.numPagerPageSize = 10;

		// 현재 페이지입니다.
		$scope.numCurrentPage = 0;

		// ### 쿼리 이벤트에 따른 결과 출력
		// #### onHead 이벤트
		// `onHead` 이벤트는, 쿼리 후 검색된 결과가 페이지 사이즈에 도달했을 때, 이 예제에서는 처음 20개의 결과를 조회할 수 있을 때 발생하는 이벤트입니다.

		// 그러나 즉시 결과를 받아올 수 있는 것은 아니며, `$helper`를 통해 넘겨진 헬퍼에 담긴 `getResult` 메소드를 통해 결과를 받아올 수 있습니다.
		$scope.onHead = function(helper) {
			// `getResult(callback)` 메소드는 callback 을 파라미터로 넘기며, 이 callback 함수의 첫번째 인자에 결과가 담겨져서 넘어옵니다.
			helper.getResult(function(message) {

				// 결과를 받아서 모델에 할당합니다.
				$scope.queryResult = message.body.result;
				$scope.$apply();
			});
		}

		// #### onStatusChange 이벤트
		// `onStatusChange` 이벤트는, 쿼리 후 상태가 변경되었을 때 발생하는 이벤트입니다. 대체로 많은 양의 테이블을 쿼리할 경우, 검색된 결과 갯수가 업데이트 되는 것을 확인할 수 있습니다.
		$scope.onStatusChange = function(message) {
			$scope.numTotalCount = message.body.count;
			$scope.$apply();
		}

		// #### onTail 이벤트
		// `onTail` 이벤트는, 쿼리가 완료되었을 때 발생하는 이벤트입니다. 헬퍼에 담긴 메시지 원본을 보면 `total_count`라는 이름으로 전체 쿼리 결과 갯수가 담겨져 오는 것을 확인 할 수 있습니다.
		$scope.onTail = function(helper) {
			$scope.numTotalCount = helper.message.body.total_count;
			$scope.$apply();
		}
		// > 참고로 `onHead`와 마찬가지로 헬퍼의 `getResult(callback)` 메소드를 이용해 마지막 20개의 결과를 조회할 수 있습니다.

		// ### 페이지 변경 이벤트
		// `on-page-change`이벤트에 `$idx`라고 인덱스를 넘겨주면, 변경된 인덱스(쪽수)를 알아낼 수 있습니다.
		$scope.changePage = function (idx) {
			// 일단 현재 페이지를 할당합니다.
			$scope.numCurrentPage = idx;

			// 원하는 구간의 쿼리 결과를 얻기 위해서는, 우선 쿼리 인스턴스를 얻어와서, 인스턴스에 내장된 `getResult` 함수를 이용합니다.

			// 이 과정에서는 두가지 주목할 점이 있는데, 첫번째는 UI 컨트롤로부터 인스턴스를 얻어오는 과정이며, 두번째로는 `getResult(offset, limit, callback)`함수를 사용하는 과정입니다. 다음은 이러한 과정에 대한 자세한 설명입니다.

			// * 먼저 css 셀렉터를 이용해서 `<query-input>` 컨트롤을 선택합니다.
			var element = angular.element('#demo-qi-1')[0];

			// * `<query-input>`에는 `getInstance`라는 메소드가 있습니다. 이를 통해 쿼리 인스턴스를 가져올 수 있습니다.
			var instance = element.getInstance();

			// * 쿼리 인스턴스의 `getResult(offset, limit, callback)` 함수를 이용합니다.
			// 얻어오고자 하는 값은 [쪽수] * [한페이지에 보여질 row 갯수] 로부터 [row 갯수]만큼입니다. 순서대로 파라미터로 넘겨줍니다.
			instance.getResult(idx * $scope.numPageSize, $scope.numPageSize, function(message) {
				$scope.queryResult = message.body.result;
				$scope.$apply()
			});

			// > **주의!**
			// > 이는 `onHead`, `onTail` 이벤트의 helper 에 딸린 `getResult(callback)` 메소드와는 넘기는 파라미터가 다릅니다.
			// > helper의 `getResult(callback)`는 컨트롤에 지정해준 대로 limit이 자동으로 지정되지만, 인스턴스의 `getResult(offset, limit, callback)`에는 offset, limit을 지정해야 합니다.
		}

		// ### 에러 처리
		// 잘못된 쿼리문을 입력할 경우 등에 대하여 에러를 표시할 필요가 있습니다.
		// 이는 `<query-input>`의 `on-failed` 이벤트를 이용해서 처리할 수 있습니다.
		// `$raw`에 에러에 대한 전체 정보가 담겨져 있습니다.
		$scope.onFailed = function(raw, type, note) {
			alert(raw[0].errorCode);
		}

		// ### 앱 종료시 쿼리 삭제
		// 위에서도 설명했듯, 쿼리 결과를 담고있는 인스턴스의 사용이 끝나면 반드시 삭제해줘야 합니다.
		// 이를 위해서 앱의 종료시의 이벤트를 등록해줍니다.

		// `eventSender[programID].$event.on(eventType, eventHandler)` 의 형태로 관련 이벤트를 등록해줄 수 있습니다.
		// 여기에는 이 프로그램의 아이디인 'logdb', 그리고 종료시의 이벤트를 지칭하는 'unload' 이벤트를 등록합니다.
		eventSender['logdb'].$event.on('unload', function() {
			var instance = angular.element('#demo-qi-1')[0].getInstance();
			serviceLogdb.remove(instance);
		});
	}

	// ### 메인 컨트롤러 등록
	// 컨트롤러를 등록해서 로그프레소가 이 앱의 컨트롤러를 인식할 수 있도록 합니다.
	extension.global.addController(SampleLogdbDirectiveController);
})();