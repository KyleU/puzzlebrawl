/* global define:false */
define(['dialog/Modal', 'state/GameState', 'utils/BrawlSync'], function (Modal, GameState, BrawlSync) {
  'use strict';

  return function(homeState, c, v) {
    switch(c) {
      case 'InitialState':
        homeState.game.userId = v.user;
        homeState.showPanel('playmat');
        break;
      case 'BrawlJoined':
        homeState.game.playmat.setBrawl(v.self, v.brawl);
        break;
      case 'PlayerUpdate':
        homeState.game.playmat.onPlayerUpdate(v);
        break;
      case 'DebugResponse':
        if(v.key === 'sync') {
          BrawlSync.check(homeState.game.playmat.brawl, JSON.parse(v.data));
        } else {
          throw 'Unhandled debug response [' + v.key + '].';
        }
        break;
      case 'PlayerLoss':
        homeState.game.playmat.onPlayerLoss(v.id);
        break;
      case 'BrawlCompletionReport':
        Modal.show('Game Complete', JSON.stringify(v, null, 2), true);
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
