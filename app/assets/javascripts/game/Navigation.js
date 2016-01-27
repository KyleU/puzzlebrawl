/* global define:false */
define(['utils/Status'], function (Status) {
  'use strict';

  function Navigation(game) {
    this.game = game;
    var self = this;

    window.addEventListener('hashchange', function() {
      var h = window.location.hash;
      if(h.charAt(0) === '#') {
        h = h.substr(1);
      }
      self.navigate(h);
      return false;
    }, false);

    var titleLabel = document.getElementById('title-text');
    if(titleLabel !== null && titleLabel !== undefined) {
      titleLabel.addEventListener('click', function() {
        window.location.hash = '#home';
      }, false);
    }
  }

  Navigation.prototype.navigate = function(key) {
    if(this.game.playmat !== undefined && this.game.playmat !== null) {
      if(this.game.playmat.brawl !== undefined && this.game.playmat.brawl !== null) {
        this.game.playmat.resignIfPlaying(key);
        return false;
      }
    }

    switch(key) {
      case '':
      case 'home':
      case 'menu':
        Status.setStatus('Home');
        this.game.panels.show('menu');
        break;
      case 'instructions':
        Status.setStatus('Instructions');
        this.game.panels.show(key);
        break;
      case 'test':
        Status.setStatus('Tests');
        this.game.panels.show(key);
        break;
      case 'scenario':
        Status.setStatus('Scenarios');
        this.game.panels.show(key);
        break;
      case 'options':
        Status.setStatus('Options');
        this.game.panels.show(key);
        break;
      case 'feedback':
        Status.setStatus('Feedback');
        this.game.panels.show(key);
        break;
      case 'status':
        Status.setStatus('Status');
        this.game.panels.show('status');
        break;
      case null:
      case undefined:
        throw 'Key was not provided.';
      default:
        Status.setStatus('Starting Brawl');
        this.game.panels.show('gameplay');
        this.game.send('StartBrawl', { 'scenario': key });
        break;
    }

    return true;
  };

  return Navigation;
});
