/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2015, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) AsyncWorldEdit contributors
 *
 * All rights reserved.
 *
 * Redistribution in source, use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2.  Redistributions of source code, with or without modification, in any form
 *     other then free of charge is not allowed,
 * 3.  Redistributions of source code, with tools and/or scripts used to build the 
 *     software is not allowed,
 * 4.  Redistributions of source code, with information on how to compile the software
 *     is not allowed,
 * 5.  Providing information of any sort (excluding information from the software page)
 *     on how to compile the software is not allowed,
 * 6.  You are allowed to build the software for your personal use,
 * 7.  You are allowed to build the software using a non public build server,
 * 8.  Redistributions in binary form in not allowed.
 * 9.  The original author is allowed to redistrubute the software in bnary form.
 * 10. Any derived work based on or containing parts of this software must reproduce
 *     the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the
 *     derived work.
 * 11. The original author of the software is allowed to change the license
 *     terms or the entire license of the software as he sees fit.
 * 12. The original author of the software is allowed to sublicense the software
 *     or its parts using any license terms he sees fit.
 * 13. By contributing to this project you agree that your contribution falls under this
 *     license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.asyncworldedit.excommands.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.command.util.CommandPermissions;
import com.sk89q.worldedit.command.util.Logging;
import com.sk89q.worldedit.command.util.Logging.LogMode;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskUnion;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.internal.annotation.Direction;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.extent.buffer.ForgetfulExtentBuffer;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.operation.OperationQueue;
import org.primesoft.asyncworldedit.worldedit.blocks.BlockStates;
import org.primesoft.asyncworldedit.worldedit.function.RegionMaskingFilterEx;
import org.primesoft.asyncworldedit.worldedit.function.block.KeepDataBlockReplace;
import org.primesoft.asyncworldedit.worldedit.function.mask.SkipDataBlockMask;

/**
 *
 * @author SBPrime
 */
public class RegionCommands {
    @Command(
            aliases = {"/replacend", "/rend", "/repnd"},
            desc = "Replace all blocks in the selection with another and keep the data"
    )
    @CommandPermissions("worldedit.region.replace")
    @Logging(LogMode.REGION)
    public int replacend(Player player, EditSession editSession, @Selection Region region,
            Mask from, Pattern to) throws WorldEditException {

        if (from == null) {
            from = new ExistingBlockMask(editSession);
        } else if (from instanceof BlockMask) {
            from = new SkipDataBlockMask((BlockMask) from);
        }

        RegionFunction replace = new KeepDataBlockReplace(editSession, to);
        RegionFunction filter = new RegionMaskingFilterEx(from, replace);
        RegionVisitor visitor = new RegionVisitor(region, filter);

        Operations.completeLegacy(visitor);

        int affected = visitor.getAffected();

        player.print(affected + " block(s) have been replaced.");

        return affected;
    }

    @Command(
            aliases = {"/stack"},
            desc = "Repeat the contents of the selection"
    )
    @CommandPermissions({"worldedit.region.stack"})
    @Logging(LogMode.ORIENTATION_REGION)
    public int stack(Player player, EditSession editSession, LocalSession session,
            @Selection Region region, int count, @Direction(includeDiagonals = true) BlockVector3 direction,
            boolean moveSelection, boolean ignoreAirBlocks,
            Mask mask) throws WorldEditException {
        final BlockVector3 size = region.getMaximumPoint().subtract(region.getMinimumPoint()).add(1, 1, 1);
        final BlockVector3 to = region.getMinimumPoint();

        final ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, editSession, to);
        copy.setRepetitions(count);
        copy.setTransform(new AffineTransform().translate(direction.multiply(size)));

        if (mask != null && ignoreAirBlocks) {
            copy.setSourceMask(new MaskUnion(mask, new ExistingBlockMask(editSession)));
        } else if (mask != null) {
            copy.setSourceMask(mask);
        } else if (ignoreAirBlocks) {
            copy.setSourceMask(new ExistingBlockMask(editSession));
        }
        Operations.completeLegacy(copy);

        int affected = copy.getAffected();

        if (moveSelection) {
            try {
                final BlockVector3 ss = region.getMaximumPoint().subtract(region.getMinimumPoint());
                final BlockVector3 shiftVector = direction.toVector3().multiply(count * (Math.abs(direction.dot(ss)) + 1)).toBlockPoint();
                region.shift(shiftVector);

                session.getRegionSelector(player.getWorld()).learnChanges();
                session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
            } catch (RegionOperationException e) {
                player.printError(e.getMessage());
            }
        }

        player.print(affected + " block(s) changed. Undo with //undo");
        return affected;
    }

    @Command(
            aliases = {"/move"},
            desc = "Move the contents of the selection"
    )
    //            "  -m sets a source mask so that excluded blocks become air\n" +
    @CommandPermissions({"worldedit.region.move"})
    @Logging(LogMode.ORIENTATION_REGION)
    public int move(Player player, EditSession editSession, LocalSession session,
            @Selection Region region, int count, @Direction(includeDiagonals = true) BlockVector3 direction,
            Pattern replace, boolean moveSelection, boolean ignoreAirBlocks,
            Mask mask) throws WorldEditException {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be >= 1");
        }

        BlockVector3 to = region.getMinimumPoint();

        // Remove the original blocks
        final Pattern pattern = replace != null ? replace : new BlockPattern(BlockStates.AIR);
        final BlockReplace remove = new BlockReplace(editSession, pattern);

        // Copy to a buffer so we don't destroy our original before we can copy all the blocks from it
        ForgetfulExtentBuffer buffer = new ForgetfulExtentBuffer(editSession, new RegionMask(region));
        
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, buffer, to);
        copy.setTransform(new AffineTransform().translate(direction.multiply(count)));

        if (mask == null) {
            copy.setSourceFunction(remove); // Remove            
        } else {
            copy.setSourceFunction(new RegionMaskingFilter(mask, remove));
        }

        copy.setRemovingEntities(true);

        if (mask != null && ignoreAirBlocks) {
            copy.setSourceMask(new MaskUnion(mask, new ExistingBlockMask(editSession)));
        } else if (mask != null) {
            copy.setSourceMask(mask);
        } else if (ignoreAirBlocks) {
            copy.setSourceMask(new ExistingBlockMask(editSession));
        }

        // Then we need to copy the buffer to the world
        BlockReplace bReplace = new BlockReplace(editSession, buffer);
        RegionVisitor visitor = new RegionVisitor(buffer.asRegion(), bReplace);

        OperationQueue operation = new OperationQueue(copy, visitor);
        Operations.completeLegacy(operation);

        int affected = copy.getAffected();

        if (moveSelection) {
            try {
                region.shift(direction.multiply(count));

                session.getRegionSelector(player.getWorld()).learnChanges();
                session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
            } catch (RegionOperationException e) {
                player.printError(e.getMessage());
            }
        }

        player.print(affected + " blocks moved.");
        
        return affected;
    }
}
