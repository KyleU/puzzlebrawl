/* global requirejs:false */
requirejs.config({
  baseUrl: '/assets/javascripts',
  paths: {
    lib: '/assets/lib'
  }
});

requirejs(['PuzzleBrawl'], function(PuzzleBrawl) {
  'use strict';
  window.puzzlebrawl = new PuzzleBrawl();
});
