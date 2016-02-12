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

    var self = this;
    submitEl.onclick = function() {
      var n = inputEl.value;
      if(n === self.game.username) {
        self.messageEl.innerText = 'You haven\'t changed your name. Choose a new one.';
      } else {
        self.submit(n);
      }
    };

    this.inputEl = inputEl;
    this.submitEl = submitEl;
    this.messageEl = document.getElementById('username-message');
  }

  ChooseName.prototype.submit = function(name) {
    this.pendingName = name;
    this.game.send('SetPreference', { name: 'username', value: name });
  };

  ChooseName.prototype.complete = function(value, status) {
    switch(status) {
      case 'already-claimed':
        this.messageEl.innerText = 'This name has already been taken, please choose another.';
        break;
      case 'ok':
        this.inputEl.value = value;
        this.messageEl.innerText = 'Ok, your name has been changed.';
        break;
      default:
        throw new Error(status);
    }
  };

  ChooseName.prototype.show = function(src) {
    this.messageEl.innerText = '';
    if(src === 'name') {
      this.game.navigation.navigate(src);
    } else {
      this.source = src;
      window.location.hash = '#name';
    }
  };

  return ChooseName;
});
