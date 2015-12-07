/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['board/Board', 'gem/Gem', 'playmat/PlaymatResizer', 'utils/Status'], function (Board, Gem, PlaymatResizer, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.boards = [];
    this.players = {};

    this.resizer = new PlaymatResizer(this);
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  Playmat.prototype.setBrawl = function(brawl) {
    if(this.brawl !== undefined) {
      throw 'Already using brawl [' + this.brawl.id + '].';
    }
    this.brawl = brawl;
    Status.setScenario(brawl.scenario);
    var playmat = this;
    _.each(brawl.players, function(p) {
      var board = new Board(p.board, p.id, playmat.game);
      playmat.boards.push(board);
      playmat.players[p.id] = {
        id: p.id,
        name: p.name,
        score: p.score,
        board: board
      };
      playmat.add(board);
    });
    this.resizer.refreshLayout();
  };

  Playmat.prototype.changeScore = function(id, delta) {
    console.log('Updating score for [' + id + '] by [' + delta + '] points.');
  };

  return Playmat;
});
