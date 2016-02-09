var gulp = require('gulp');
var del = require('del');
var inject = require('gulp-inject');
var browserSync = require('browser-sync');

var concat = require('gulp-concat');

var less = require('gulp-less');
var uglify = require('gulp-uglify');
var minifyCSS = require('gulp-minify-css');
var minifyHTML = require('gulp-minify-html');

//var jshint = require('gulp-jshint');
var sourceMaps = require('gulp-sourcemaps');

var karma = require('gulp-karma');

//=========================================== configs ===========================================

var srcFolder = '../src/main';
var testsFolder = '../src/test';
var devTargetFolder = '../../public/';
var devLibFolder = devTargetFolder + '/lib';

var config = {
    indexSource: srcFolder + '/index.html',
    htmlSource: [
        srcFolder + '/**/*.html'
    ],
    jsSource: [
        srcFolder + '/app.js',
        srcFolder + '/injectables/**/*.js',
        srcFolder + '/pages/**/*.js'
    ],
    lessSource: [
        srcFolder + '/**/*.less'
    ],
    bowerJsForTest: [
        'bower_components/angular-mocks/angular-mocks.js'
    ],
    jsTestSource: [
        testsFolder + '/**/*.js'
    ]
};

var devConfig = {
    bowerJs: [
        'bower_components/jquery/dist/jquery.js',
        'bower_components/angular/angular.js',
        'bower_components/angular-ui-router/release/angular-ui-router.js',
        'bower_components/bootstrap/dist/js/bootstrap.js'
    ],
    bowerCss: [
        'bower_components/bootstrap/dist/css/bootstrap.css',
        'bower_components/bootstrap/dist/css/bootstrap.css.map'
    ],
    libJs: [
        devLibFolder + '/jquery.js',
        devLibFolder + '/angular.js',
        devLibFolder + '/angular-ui-route.js',
        devLibFolder + '/**/*.js'
    ],
    libCss: [
        devLibFolder + '/**/*.css'
    ]
};

//=========================================== helpers ===========================================

function copy(srcMask, destFolder) {
    return gulp.src(srcMask).pipe(gulp.dest(destFolder))
}

function bower2lib(bowerSources, libFolder) {
    del.sync(libFolder);
    return copy(bowerSources, libFolder);
}

function fillIndex(indexSource, sourcesToFill, indexFinalDestination) {
    var gulpedSources = gulp.src(sourcesToFill, {read: false});

    return gulp.src(indexSource)
        .pipe(gulp.dest(indexFinalDestination))
        .pipe(inject(gulpedSources, {relative: true}))
        .pipe(gulp.dest(indexFinalDestination));
}

//==================================== developer environment ====================================

gulp.task('dev.clean', function(callback) {
    return del(devTargetFolder, callback);
});

gulp.task('dev.bower2pack', function(cb) {
    var bowerSources = devConfig.bowerJs.concat(devConfig.bowerCss);
    return bower2lib(bowerSources, devLibFolder);
});

gulp.task('dev.js2pack', function() {
    return gulp.src(config.jsSource)
        .pipe(sourceMaps.init())
        //.pipe(jshint())
        //.pipe(jshint.reporter('default'))
        .pipe(concat('app.js'))
        .pipe(uglify())
        .pipe(sourceMaps.write('js-maps'))
        .pipe(gulp.dest(devTargetFolder))
});

gulp.task('dev.less2pack', function() {
    return gulp.src(config.lessSource)
        .pipe(sourceMaps.init())
        .pipe(less())
        .pipe(concat('app.css'))
        .pipe(minifyCSS())
        .pipe(sourceMaps.write('css-maps'))
        .pipe(gulp.dest(devTargetFolder))
});

gulp.task('dev.html2pack', function() {
    return gulp.src(config.htmlSource)
        .pipe(minifyHTML())
        .pipe(gulp.dest(devTargetFolder));
});

gulp.task('dev.index2pack', function() {
    var addToIndexSources =
        devConfig.libJs
            .concat(devConfig.libCss)
            .concat(devTargetFolder + '/app.js')
            .concat(devTargetFolder + '/app.css');

    return fillIndex(config.indexSource, addToIndexSources, devTargetFolder)
        .pipe(minifyHTML())
        .pipe(gulp.dest(devTargetFolder));
});

gulp.task('dev.browserSync.start', function() {
    browserSync({
        server: {
            baseDir: devTargetFolder
        },
        port: 9090,
        https: false,
        ui: {
            port: 9091
        },
        notify: false,
        ghostMode: false,
        //browser: 'google-chrome-stable'
        browser: 'firefox'
    });
});


gulp.task('dev.watch', function() {
    // Organize synchronizing browserSync restarts (if js and less files changed simultaneously)
    var locks = {};

    function getLockTask(lockName) {
        return function(callback) {
            locks[lockName] = true;
            callback();
        }
    }

    function getUnLockTask(lockName) {
        return function(callback) {
            locks[lockName] = false;
            callback();
        }
    }

    gulp.task('dev.browserSync.reloadAfterUnlocks', function(callback) {
        function isLocked() {
            for (key in Object.keys(locks)) {
                if (locks[key]) return true;
            }
            return false;
        }

        if (!isLocked()) browserSync.reload();
        callback();
    });

    // update bower
    var bowerSources = devConfig.bowerJs.concat(devConfig.bowerCss);
    gulp.watch(bowerSources, gulp.series(getLockTask('bower2pack'), 'dev.bower2pack', getUnLockTask('bower2pack'), 'dev.browserSync.reloadAfterUnlocks'));

    // update js
    gulp.watch(config.jsSource, gulp.series(getLockTask('js2pack'), 'dev.js2pack', getUnLockTask('js2pack'), 'dev.browserSync.reloadAfterUnlocks'));

    // update less
    gulp.watch(config.lessSource, gulp.series(getLockTask('less2pack'), 'dev.less2pack', getUnLockTask('less2pack'), 'dev.browserSync.reloadAfterUnlocks'));

    // update html
    gulp.watch(config.htmlSource, gulp.series(getLockTask('html2pack'), 'dev.html2pack', 'dev.index2pack', getUnLockTask('html2pack'), 'dev.browserSync.reloadAfterUnlocks'));
});

gulp.task('dev', gulp.series(
    'dev.clean',
    gulp.parallel('dev.bower2pack', 'dev.js2pack', 'dev.less2pack', 'dev.html2pack'),
    'dev.index2pack',
    gulp.parallel('dev.browserSync.start', 'dev.watch')
));