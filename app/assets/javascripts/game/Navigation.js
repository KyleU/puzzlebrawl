/* global define:false */
define([], function () {
  'use strict';

  function Navigation(game) {
    this.game = game;
    var self = this;
    window.addEventListener('hashchange', function() {
      self.navigate();
    }, false);
  }

  Navigation.prototype.navigate = function() {
    switch(window.location.hash) {
      case '':
        this.game.panels.show('menu');
        break;
      case '#normal':
        this.game.panels.show('gameplay');
        this.game.send('StartBrawl', { 'scenario': 'normal'});
        break;
      default:
        console.log('Unhandled navigation: ' + window.location.hash);
        break;
    }
  };

  return Navigation;
});
