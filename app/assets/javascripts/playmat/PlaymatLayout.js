/* global define:false */
/* global _:false */
define(['utils/Config'], function (Config) {
  'use strict';

  //var targetAspectRatios = [
  //  9 / 16,
  //  3 / 4,
  //  4 / 3,
  //  16 / 9
  //];

  var marginPx = 32;

  var PlaymatLayout = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatLayout.prototype.closestAspectRatio = function() {
    //var world = this.playmat.game.world;
    //var aspectRatio = world.width / world.height;
    //var closestRatio = Phaser.ArrayUtils.findClosest(aspectRatio, targetAspectRatios);
    //console.log(targetAspectRatios, aspectRatio, closestRatio);
  };

  PlaymatLayout.prototype.refreshLayout = function() {
    var p = this.playmat;

    this.closestAspectRatio();

    var selfId = this.playmat.self;
    if(selfId === null) {
      throw 'No self id.';
    }

    var originalSize = [p.w, p.h];

    var splitPlayers = _.partition(p.players, function(player) { return player.id === selfId; });

    if(splitPlayers.length !== 2 || splitPlayers[0].length !== 1) {
      throw 'Incomplete board definitions.';
    }

    var self = splitPlayers[0][0];
    var selfScale = 1;

    var xOffset = marginPx;

    self.labels.resize(xOffset, self.board.w * Config.tile.size * selfScale, marginPx + (self.board.h * Config.tile.size * selfScale));

    self.board.x = xOffset;
    self.board.y = marginPx;

    self.board.scale = { x: selfScale, y: selfScale };
    xOffset += self.board.width + marginPx;

    if(splitPlayers[1].length > 0) {
      xOffset = renderOthers(splitPlayers[1], xOffset);
    }

    p.w = xOffset;
    p.h = (self.board.h + 1) * Config.tile.size;

    if(p.w !== originalSize[0] || p.h !== originalSize[1]) {
      this.playmat.resizer.resize();
    }
  };

  function renderOthers(others, originalOffset) {
    var boardsPerRow;
    var othersScale;

    if(others.length < 5) {
      boardsPerRow = others.length;
      othersScale = 0.8;
    } else if(others.length < 12) {
      boardsPerRow = Math.ceil(others.length / 2);
      othersScale = 0.4;
    } else {
      boardsPerRow = Math.ceil(others.length / 4);
      othersScale = 0.2;
    }

    var xOffset;
    var maxOffset = 0;

    _.each(others, function(player, idx) {
      var row = Math.floor(idx / boardsPerRow);

      if(idx % boardsPerRow === 0) {
        xOffset = originalOffset;
      }

      if(boardsPerRow > 5) {
        player.labels.hide();
      } else {
        var offsetY = marginPx + ((row + 1) * (player.board.h * Config.tile.size * othersScale)) + (row * 64);
        player.labels.resize(xOffset, player.board.w * Config.tile.size * othersScale, offsetY);
      }

      player.board.x = xOffset;
      player.board.y = marginPx + (row * ((player.board.h * Config.tile.size * othersScale) + 64));

      player.board.scale = { x: othersScale, y: othersScale };

      xOffset += player.board.width + marginPx;
      if(xOffset > maxOffset) {
        maxOffset = xOffset;
      }
    });

    return maxOffset;
  }

  return PlaymatLayout;
});
