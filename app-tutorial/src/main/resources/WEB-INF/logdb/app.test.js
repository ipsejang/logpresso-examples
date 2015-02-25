'use strict';
location._testPort_ = '8888';

describe('myApp.view1 module', function() {

	beforeEach(module('app', 'config.block'));

	beforeEach(inject(function() {
		var flag = false;
		runs(function() {
			$.getScript('base/logdb/app.js')
			.done(function() {
				console.log('script loaded');
				flag = true;
			})
			.fail(function( jqxhr, settings, exception) {
				console.log(exception);
			});
		});

		waitsFor(function() {
			return flag;
		}, 1000);

	}));


	it('....', function() {
		var flag = false;
		runs(function() {
			console.log('....')
			setTimeout(function() {
				flag = true;
			}, 500)
		});

		waitsFor(function() {
			return flag;
		}, 1000);
	});

	it('....', inject(function(_serviceUtility_) {
		console.log('sss')
		console.log(_serviceUtility_.exists);
		console.log(app.register)
	}));

	it('....', inject(function($rootScope, $controller) {
		var scope = $rootScope.$new();
		var view1Ctrl = $controller('SampleLogdbDirectiveController', {$scope: scope, $element: angular.element('<div></div>')});
		expect(view1Ctrl).toBeDefined();
	}));

});