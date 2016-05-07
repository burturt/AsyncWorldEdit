/*
 * AsyncWorldEdit Premium is a commercial version of AsyncWorldEdit. This software 
 * has been sublicensed by the software original author according to p7 of
 * AsyncWorldEdit license.
 *
 * AsyncWorldEdit Premium - donation version of AsyncWorldEdit, a performance 
 * improvement plugin for Minecraft WorldEdit plugin.
 *
 * Copyright (c) 2015, SBPrime <https://github.com/SBPrime/>
 *
 * All rights reserved.
 *
 * 1. You may: 
 *    install and use AsyncWorldEdit in accordance with the Software documentation
 *    and pursuant to the terms and conditions of this license
 * 2. You may not:
 *    sell, redistribute, encumber, give, lend, rent, lease, sublicense, or otherwise
 *    transfer Software, or any portions of Software, to anyone without the prior 
 *    written consent of Licensor
 * 3. The original author of the software is allowed to change the license 
 *    terms or the entire license of the software as he sees fit.
 * 4. The original author of the software is allowed to sublicense the software 
 *    or its parts using any license terms he sees fit.
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
package org.primesoft.asyncworldedit.api;

import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacer;
import org.primesoft.asyncworldedit.api.directChunk.IDirectChunkAPI;
import org.primesoft.asyncworldedit.api.map.IMapUtils;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerManager;
import org.primesoft.asyncworldedit.api.progressDisplay.IProgressDisplayManager;
import org.primesoft.asyncworldedit.api.taskdispatcher.ITaskDispatcher;

/**
 * The main AsyncWorldEdit API class
 * @author SBPrime
 */
public interface IAsyncWorldEdit {    
    /**
     * Get the progress display manager
     * @return 
     */
    IProgressDisplayManager getProgressDisplayManager();
    
    
    /**
     * Get the task dispatcher
     * @return 
     */
    ITaskDispatcher getTaskDispatcher();
    
    
    /**
     * Get the block placer
     * @return 
     */
    IBlockPlacer getBlockPlacer();
    
    
    /**
     * Get the physics watcher
     * @return 
     */
    IPhysicsWatch getPhysicsWatcher();
    

    /**
     * The player manager
     * @return 
     */
    IPlayerManager getPlayerManager();
    
    
    /**
     * Get the direct chunk API
     * @return 
     */
    IDirectChunkAPI getDirectChunkAPI();
    
    
    /**
     * Get the native API adapter
     * @return 
     */
    IAdapter getAdapter();
    
    /**
     * Get the current version of the API
     * @return 
     */
    double getAPIVersion();
    
    /**
     * Get the map manipulation utils
     * @return 
     */
    IMapUtils getMapUtils();
}
