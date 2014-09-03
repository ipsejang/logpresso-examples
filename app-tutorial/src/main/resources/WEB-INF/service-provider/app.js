(function() {

	function ServiceHelloWorld(socket, serviceLogdb) {
		// You can use logpresso internal services
		console.log(socket, serviceLogdb);
		return {
			hello: function(msg) {
				console.log('call hello', msg);
				return 'hello, ' + msg;
			},
			world: function() {
				console.log('call world');
				return 'world';
			}
		}
	}
	
	extension.global.addService('serviceHelloWorld', ServiceHelloWorld);

	function ServiceProviderDemoController($scope) {

	}

	extension.global.addController(ServiceProviderDemoController);
})()