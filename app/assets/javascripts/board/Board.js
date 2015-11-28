/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['gem/Gem'], function (Gem) {
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
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.at = function(x, y) {
    return _.find(this.gemLocations, function(gemLoc) {
      return gemLoc[1] === x && gemLoc[2] === y;
    });
  };

  Board.prototype.addActiveGem = function(gem, x, y) {
    var g = new Gem(gem, this.game);
    g.x = x * 128;
    g.y = this.height - (y * 128);
    this.activeGemLocations.push([g, x, y]);
    this.add(g);
  };

  Board.prototype.addGem = function(gem, x, y) {
    var original = this.gemLocations[gem.id];
    if(original !== null && original !== undefined) {
      throw 'Gem [' + gem.id + '] has already been added.';
    }
    var activeGem = _.find(this.activeGemLocations, function(ag) {
      return ag[0].model.id === gem.id;
    });

    if(activeGem === undefined) {
      var g = new Gem(gem, this.game);
      g.x = x * 128;
      g.y = this.height - (y * 128);
      this.gemLocations[gem.id] = [g, x, y];
      this.add(g);
    } else {
      this.activeGemLocations = _.reject(this.activeGemLocations, function(g) {
        return g[0].model.id === gem.id;
      });
      this.gemLocations[gem.id] = activeGem;
      this.moveGem(activeGem[1], activeGem[2], x - activeGem[1], y - activeGem[2]);
    }
  };

  Board.prototype.moveActiveGems = function(ags) {
    for(var agIdx = 0; agIdx < ags.length; agIdx++) {
      var g = this.activeGemLocations[agIdx];
      var newLoc = ags[agIdx];
      this.activeGemLocations[agIdx] = [g[0], newLoc.x, newLoc.y];
      var tween = this.game.add.tween(g[0]);
      tween.to({x: newLoc.x * 128, y: this.height - (newLoc.y * 128) }, 200, Phaser.Easing.Cubic.Out);
      tween.start();
    }
  };

  Board.prototype.changeGem = function(newGem, x, y) {
    var g = this.at(x, y);
    g[0].updateModel(newGem);
  };

  Board.prototype.moveGem = function(x, y, xDelta, yDelta) {
    var g = this.at(x, y);
    if(g === null || g === undefined) {
      throw 'Gem at [' + x + ', ' + y + '] is not present.';
    }
    this.gemLocations[g[0].model.id] = [g[0], x + xDelta, y + yDelta];
    var tween = this.game.add.tween(g[0]);
    tween.to({x: (x + xDelta) * 128, y: this.height - ((y + yDelta) * 128) }, 200, Phaser.Easing.Cubic.Out);
    tween.start();
  };

  Board.prototype.removeGem = function(x, y) {
    var g = this.at(x, y);
    if(g === null || g === undefined) {
      throw 'Gem at [' + x + ', ' + y + '] has not been added.';
    }
    this.gemLocations[g.id] = null;
    this.remove(g[0]);
  };

  Board.prototype.setActiveGems = function(ags) {
    if(this.activeGemLocations.length === 0) {
      for(var newIdx = 0; newIdx < ags.length; newIdx++) {
        var newAg = ags[newIdx];
        this.addActiveGem(newAg.gem, newAg.x, newAg.y);
      }
    } else {
      for(var agIdx = 0; agIdx < ags.length; agIdx++) {
        var original = this.activeGemLocations[agIdx];
        var ag = ags[agIdx];
        if(original[0].id !== ag.id) {
          throw 'Incorrect active gem at index [' + agIdx + '].';
        }
      }
      this.moveActiveGems(ags);
    }
  };

  return Board;
});
