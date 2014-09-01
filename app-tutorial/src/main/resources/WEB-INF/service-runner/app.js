(function() {
	function ServiceRunnerDemoController($scope, $injector) {
		var serviceHelloWorld = $injector.instantiate(extension.services.serviceHelloWorld);
		$scope.hello = serviceHelloWorld.hello('Hello world service injected');
	}

	extension.global.addController(ServiceRunnerDemoController);
})()