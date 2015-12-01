/* global define:false */
/* global Phaser:false */
/* global _:false */
define([
  'gem/Gem', 'board/BoardGems', 'board/BoardActiveGems', 'board/BoardMutations', 'board/BoardScore'
], function (Gem, BoardGems, BoardActiveGems, BoardMutations, BoardScore) {
  'use strict';

  function Board(model, game) {
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;

    this.activeGemLocations = [];

    this.gemLocations = {};
    Phaser.Group.call(this, game, null, 'board-' + model.key);

    game.add.existing(this);
    this.bgTileSprite = new Phaser.TileSprite(game, 0, 0, 128 * this.w, 128 * this.h, 'board-bg-a');

    this.bgTileSprite.name = 'background';
    this.add(this.bgTileSprite);

    for(var y = 0; y < this.h; y++) {
      for(var x = 0; x < this.w; x++) {
        var g = model.spaces[x][y];
        if(g !== null && this.gemLocations[g.id] === undefined) {
          this.addGem(g, x, y);
        }
      }
    }

    BoardScore.init(this, model.score);
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.changeScore = function(delta) {
    BoardScore.changeScore(this, delta);
  };

  Board.prototype.at = function(x, y) {
    return _.find(this.gemLocations, function(gemLoc) {
      return gemLoc[1] === x && gemLoc[2] === y;
    });
  };

  Board.prototype.applyMutations = function(mutations) {
    BoardMutations.applyMutations(this, mutations);
  };

  Board.prototype.setActiveGems = function(ags) {
    BoardActiveGems.setActiveGems(this, ags);
  };

  Board.prototype.addGem = function(gem, x, y) {
    BoardGems.addGem(this, gem, x, y);
  };

  Board.prototype.changeGem = function(newGem, x, y) {
    BoardGems.changeGem(this, newGem, x, y);
  };

  Board.prototype.moveGem = function(x, y, xDelta, yDelta) {
    BoardGems.moveGem(this, x, y, xDelta, yDelta);
  };

  Board.prototype.removeGem = function(x, y) {
    BoardGems.removeGem(this, x, y);
  };

  return Board;
});
