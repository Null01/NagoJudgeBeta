package edu.nagojudge.app.utils.constants;

import edu.nagojudge.app.utils.FacesUtil;
import java.io.File;

/**
 *
 * @author andresfelipegarciaduran
 */
public interface IResourcesPaths {

    public final String PATH_ROOT_WORKSPACE_LOCAL = System.getProperty("user.dir") + File.separatorChar + "workspace";
    public final String PATH_ROOT_WORKSPACE_RESOURCE_WEB = FacesUtil.getFacesUtil().getRealPath() + "resources";
    public final String PATH_ROOT_SAVE_PROBLEMS_WEB = FacesUtil.getFacesUtil().getRealPath() + "external" + File.separatorChar + "problems";

    public final String PATH_WORKSPACE_IMG_RESOURCE_WEB = FacesUtil.getFacesUtil().getRealPath() + "resources" + File.separatorChar + "img";
    public final String PATH_SAVE_PROBLEMS_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "problems";
    public final String PATH_SAVE_INPUT_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "inputs";
    public final String PATH_SAVE_OUTPUT_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "outputs";
    public final String PATH_SAVE_CODE_SOURCE_LOCAL = PATH_ROOT_WORKSPACE_LOCAL + File.separatorChar + "users";

    public final String PATH_VIEW_PROBLEMS = File.separatorChar + "go" + File.separatorChar + "to" + File.separatorChar + "external" + File.separatorChar + "problems";

}
