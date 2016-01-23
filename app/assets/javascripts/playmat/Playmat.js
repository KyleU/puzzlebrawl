/* global define:false */
/* global Phaser:false */
define([
  'board/Board', 'gem/Gem',
  'playmat/PlaymatLayout', 'playmat/PlaymatResizer', 'playmat/PlaymatSetBrawl', 'playmat/PlaymatTargets',
  'utils/Formatter'
], function (Board, Gem, PlaymatLayout, PlaymatResizer, setBrawl, PlaymatTargets, Formatter) {
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
    setBrawl(this, self, brawl);
  };

  Playmat.prototype.onPlayerUpdate = function(update) {
    var p = this;
    var board = p.players[update.id].board;
    if(board === undefined || board === null) {
      throw 'Player update received with invalid id [' + update.id + '].';
    }
    board.applyMutations(update.segments, function(delta) {
      p.changeScore(update.id, delta);
    });
  };

  Playmat.prototype.changeScore = function(id, delta) {
    var player = this.players[id];
    player.score += delta;
    player.labels.scoreLabel.text = Formatter.withCommas(player.score);
  };

  Playmat.prototype.onPlayerLoss = function(playerId) {
    this.players[playerId].board.alpha = 0.5;
  };

  Playmat.prototype.resignIfPlaying = function() {
    if(this.brawl !== null && this.brawl !== undefined) {
      console.log('Resign');
    }
  };

  return Playmat;
});
