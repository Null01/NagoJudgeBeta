package edu.nagojudge.live.web.utils.constants;

import edu.nagojudge.live.web.utils.FacesUtil;
import java.io.File;

/**
 *
 * @author andresfelipegarciaduran
 */
public interface IResourcesPath {

    public final String PATH_ROOT_WORKSPACE_LOCAL = System.getProperty("user.dir") + File.separatorChar + "workspace-nagojudge";
    
    public final String PATH_ROOT_WORKSPACE_RESOURCE_WEB = FacesUtil.getFacesUtil().getRealPath() + "resources";
    
    public final String PATH_ROOT_SAVE_PROBLEMS_WEB = FacesUtil.getFacesUtil().getRealPath() + "external" + File.separatorChar + "problems";
    public final String PATH_WORKSPACE_IMG_RESOURCE_WEB = FacesUtil.getFacesUtil().getRealPath() + "resources" + File.separatorChar + "img";
    
    public final String PATH_SAVE_PROBLEMS_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "problems";
    public final String PATH_SAVE_INPUT_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "content" + File.separatorChar + "inputs";
    public final String PATH_SAVE_OUTPUT_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "content" + File.separatorChar + "outputs";
    public final String PATH_SAVE_CODE_SOURCE_LOCAL_TM = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "teams";

    public final String PATH_VIEW_PROBLEMS = File.separatorChar + "now" + File.separatorChar + "external" + File.separatorChar + "problems";

}
