module.exports = function(config){
  var absPath = '/Users/gotoweb/webfx/src/main/resources/WEB-INF/';
  var confjson = {

    basePath : './',

    files : [
      absPath+'lib/cryptojs/sha1.js',
      absPath+'lib/jquery/jquery-2.1.1.js',
      absPath+'lib/jquery/jquery-ui.js',
      absPath+'lib/angular/angular-1.2.26.js',
      absPath+'lib/angular/angular-route.js',
      absPath+'lib/angular/angular-translate.js',
      absPath+'lib/angular/angular-translate-loader-partial.js',
      absPath+'lib/angular/angular-ui-router.js',
      absPath+'lib/angular/ct-ui-router-extras.js',
      absPath+'lib/bootstrap2/js/bootstrap.js',
      {
        pattern:  absPath+'locales/ko.json',
        watched:  true,
        served:   true,
        included: false
      },
      {
        pattern:  absPath+'apps/default-assets/**/*.js',
        watched:  true,
        served:   true,
        included: false
      },
      absPath+'lib/d3/d3.js',
      absPath+'lib/d3/d3.layout.cloud.js',
      absPath+'node_modules/ng-midway-tester/src/ngMidwayTester.js',
      absPath+'bower_components/angular-mocks/angular-mocks.js',
      absPath+'lib/highcharts/highcharts.js',
      absPath+'test/unit/index.mock.js',
      absPath+'script/app-initializer.js',
      absPath+'script/service/*.js',
      absPath+'script/directive/*.js',
      absPath+'script/filter/*.js',
      absPath+'script/worker/*.js',
      {
        pattern:  absPath+'script/worker/*.js',
        watched:  true,
        served:   true,
        included: false
      },
      {
        pattern:  absPath+'test/resources/*.json',
        watched:  true,
        served:   true,
        included: false
      },
      absPath+'script/directive/*.html',
      absPath+'css/widget.css',
      absPath+'lib/bootstrap2/css/*',
      {
        pattern:  absPath+'lib/bootstrap2/img/*',
        watched:  true,
        served:   true,
        included: false
      },
      absPath+'css/ui-cronizer.css',
      absPath+'css/table-view-resizable.css',

      {
        pattern:  'logdb/app.js',
        watched:  true,
        served:   true,
        included: false
      },
      'logdb/app.test.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-ng-html2js-preprocessor'
            ],

    preprocessors: {},

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    },

    proxies: {}

  }

  confjson.preprocessors[absPath + 'script/directive/*.html'] = ['ng-html2js'];
  confjson.proxies['/script/worker/'] = 'http://localhost:9876/absolute/Users/gotoweb/webfx/src/main/resources/WEB-INF/script/worker/';
  config.set(confjson);
};
