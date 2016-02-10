/* global define:false */
define([], function() {
  'use strict';

  function ChooseName(game) {
    this.game = game;
    var inputEl = document.getElementById('username-input');
    var submitEl = document.getElementById('username-submit');
    if(inputEl === null || submitEl === null) {
      throw new Error('Missing elements.');
    }
    
    submitEl.onclick = function() {
      console.log(inputEl.value);
    };

    this.inputEl = inputEl;
    this.submitEl = submitEl;
  }

  ChooseName.prototype.show = function(src) {
    if(src === 'name') {
      this.game.navigation.navigate(src);
    } else {
      this.source = src;
      window.location.hash = '#name';
    }
  };

  return ChooseName;
});
