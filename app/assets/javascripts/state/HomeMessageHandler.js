/* global define:false */
define(['panels/Modal', 'state/GameState', 'utils/BrawlSync'], function (Modal, GameState, BrawlSync) {
  'use strict';

  return function(homeState, c, v) {
    switch(c) {
      case 'InitialState':
        homeState.initialStateReceived(v);
        break;
      case 'BrawlQueueUpdate':
        homeState.game.matchmaking.show(v);
        break;
      case 'BrawlJoined':
        homeState.game.playmat.setBrawl(v.self, v.brawl);
        homeState.game.panels.show('gameplay');
        break;
      case 'PlayerUpdate':
        homeState.game.playmat.onPlayerUpdate(v);
        break;
      case 'DebugResponse':
        if(v.key === 'sync') {
          BrawlSync.check(homeState.game.playmat.brawl, JSON.parse(v.data));
        } else {
          throw new Error('Unhandled debug response [' + v.key + '].');
        }
        break;
      case 'PlayerLoss':
        homeState.game.playmat.onPlayerLoss(v.id);
        break;
      case 'BrawlCompletionReport':
        Modal.show('Game Complete', JSON.stringify(v, null, 2), true);
        homeState.game.gameInput.pause();
        break;
      case 'PreferenceChanged':
        homeState.options.preferenceChanged(v.name, v.value, v.result);
        break;
      case 'ServerError':
        Modal.show('Server Error', v.reason + ': ' + v.content);
        break;
      default:
        GameState.prototype.onMessage.call(homeState, c, v);
        break;
    }
  };
});
