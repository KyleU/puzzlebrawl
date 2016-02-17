/* global define:false */
/* global Phaser:false */
define([
  'board/Board', 'gem/Gem',
  'playmat/PlaymatLayout', 'playmat/PlaymatResizer', 'playmat/PlaymatBrawl', 'playmat/PlaymatTargets',
  'utils/Formatter'
], function (Board, Gem, PlaymatLayout, PlaymatResizer, PlaymatBrawl, PlaymatTargets, Formatter) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.players = {};
    this.self = null;

    this.visible = true;

    this.layout = new PlaymatLayout(this);
    this.resizer = new PlaymatResizer(this);
    this.targets = new PlaymatTargets(this);
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  Playmat.prototype.setBrawl = function(self, brawl) {
    PlaymatBrawl.startBrawl(this, self, brawl);
  };

  Playmat.prototype.onPlayerUpdate = function(update) {
    var p = this;
    var board = p.players[update.id].group.board;
    if(board === undefined || board === null) {
      throw new Error('Player update received with invalid id [' + update.id + '].');
    }
    board.applyMutations(update.segments, function(delta) {
      p.changeScore(update.id, delta);
    });
  };

  Playmat.prototype.changeScore = function(id, delta) {
    var player = this.players[id];
    player.score += delta;
    player.group.labels.scoreLabel.text = Formatter.withCommas(player.score);
  };

  Playmat.prototype.onPlayerLoss = function(playerId) {
    if(this.players !== undefined && this.players[playerId] !== undefined) {
      this.players[playerId].group.board.alpha = 0.5;
    }
    if(PlaymatBrawl.pendingNav !== undefined && PlaymatBrawl.pendingNav !== null) {
      this.game.navigation.navigate(PlaymatBrawl.pendingNav);
      PlaymatBrawl.pendingNav = null;
    }
  };

  Playmat.prototype.resignIfPlaying = function(pendingNav) {
    if(this.brawl !== null && this.brawl !== undefined) {
      PlaymatBrawl.pendingNav = pendingNav;
      PlaymatBrawl.resignBrawl(this);
    }
  };

  return Playmat;
});
