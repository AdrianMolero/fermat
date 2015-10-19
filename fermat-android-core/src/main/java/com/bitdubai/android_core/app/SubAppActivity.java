package com.bitdubai.android_core.app;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bitdubai.android_core.app.common.version_1.FragmentFactory.SubAppFragmentFactory;
import com.bitdubai.android_core.app.common.version_1.navigation_drawer.NavigationDrawerFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.ActivityType;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppSessionManager;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppsSession;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.*;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatCallback;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatScreenSwapper;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.*;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledSubApp;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledWallet;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_publisher.interfaces.WalletPublisherModuleManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_sub_app_module.wallet_store.interfaces.WalletStoreModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedUIExceptionSeverity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bitdubai.fermat.R;


/**
 * Created by Matias Furszyfer
 */


public class SubAppActivity extends FermatActivity implements FermatScreenSwapper, NavigationDrawerFragment.NavigationDrawerCallbacks{

    private static final String INSTALLED_SUB_APP = "sub_app";
    /**
     * Members used by back button
     */
    private String actionKey;
    private Object[] screenObjects;



    /**
     *  Called when the activity is first created
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityType(ActivityType.ACTIVITY_TYPE_SUB_APP);

        try {

            loadUI(createOrCallSubAppSession());

        } catch (Exception e) {

            //reportUnexpectedUICoreException
            //hacer un enum con areas genericas
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.drawer_layout));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }



    /**
     * This method replaces the current fragment by the next in navigation
     * @param fragmentType Type Id of fragment to show
     */

    private void loadFragment(SubApps subApp,int idContainer, String fragmentType) throws InvalidParameterException {


        SubAppSessionManager subAppSessionManager = ((ApplicationSession) getApplication()).getSubAppSessionManager();
        SubAppsSession subAppsSession = subAppSessionManager.getSubAppsSession(getSubAppRuntimeMiddleware().getLastSubApp().getType());


        try {
            getSubAppRuntimeMiddleware().getLastSubApp().getLastActivity().getFragment(fragmentType);
            com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppFragmentFactory subAppFragmentFactory = SubAppFragmentFactory.getFragmentFactoryBySubAppType(subApp);


            android.app.Fragment fragment = subAppFragmentFactory.getFragment(
                    fragmentType,
                    subAppsSession,
                    null, //getSubAppSettingsManager().getSettings(xxx),
                    getSubAppResourcesProviderManager()
            );

            FragmentTransaction FT = this.getFragmentManager().beginTransaction();
            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            FT.replace(idContainer, fragment);

            FT.commit();
        } catch (Exception e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, e);
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu
     * @param menu
     * @return true if all is okey
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        /**
         *  Our future code goes here...
         */

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item
     * @return true if button is clicked
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        try {


            int id = item.getItemId();


            /**
             *  Our future code goes here...
             */

        } catch (Exception e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, e);
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }

        return false;
    }


    /**
     * This method catch de back bottom event and decide which screen load
     */

    @Override
    public void onBackPressed() {



        // get actual fragment on execute
        String frgBackType = null;
        try {
            SubAppRuntimeManager subAppRuntimeManager = getSubAppRuntimeMiddleware();

            com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Fragment fragment=null;

            Activity activity = null;
            try{

                activity= subAppRuntimeManager.getLastSubApp().getLastActivity();
                fragment  = activity.getLastFragment();

            }catch (NullPointerException nullPointerException){
                fragment=null;
            }



            //get setting fragment to back
            //if not fragment to back I back to desktop

            if (fragment != null)
                frgBackType = fragment.getBack();


            if (frgBackType != null) {

                Activity activities = getSubAppRuntimeMiddleware().getLastSubApp().getLastActivity();

                com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Fragment fragmentBack = activities.getFragment(frgBackType); //set back fragment to actual fragment to run


                //TODO: ver como hacer para obtener el id del container
                if(fragmentBack.getType().equals("CSADDTD") || fragmentBack.getType().equals("CSADDTT") || fragmentBack.getType().equals("CSADDTR")  || fragmentBack.getType().equals("CSADDT")){
                    this.loadFragment(subAppRuntimeManager.getLastSubApp().getType(), R.id.logContainer,frgBackType);
                }else {
                    this.loadFragment(subAppRuntimeManager.getLastSubApp().getType(), R.id.startContainer,frgBackType);
                }

            }else if(activity!=null && activity.getBackActivity()!=null){
                changeActivity(activity.getBackActivity().getCode());

            } else {
                // set Desktop current activity
                activity = getSubAppRuntimeMiddleware().getLastSubApp().getLastActivity();
                if (activity.getType() != Activities.CWP_WALLET_MANAGER_MAIN) {
                    resetThisActivity();
                    //getSubAppRuntimeMiddleware().getHomeScreen();
                    getSubAppRuntimeMiddleware().getSubApp(SubApps.CWP_WALLET_MANAGER);
                    getSubAppRuntimeMiddleware().getLastSubApp().getActivity(Activities.CWP_WALLET_MANAGER_MAIN);
                    //cleanWindows();

                    loadUI(createOrCallSubAppSession());
                } else {
                    super.onBackPressed();
                }
            }
        }catch (Exception e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, e);
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * ScreenSwapper interface implementation
     */


    /**
     * This Method execute the navigation to an other fragment or activity
     * Get button action of screen
     */

    @Override
    public void changeScreen(String screen,int idContainer,Object[] objects) {

        try {

            SubAppRuntimeManager subAppRuntimeManager= getSubAppRuntimeMiddleware();

            loadFragment(subAppRuntimeManager.getLastSubApp().getType(),idContainer, screen);

        } catch (Exception e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in changeWalletFragment"));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void selectWallet(InstalledWallet installedWallet){
        Intent intent;

        try {

            //Activities activityType = Activities.getValueFromString(this.actionKey);


            WalletNavigationStructure walletNavigationStructure= getWalletRuntimeManager().getWallet(installedWallet.getWalletPublicKey());

            intent = new Intent(this, com.bitdubai.android_core.app.WalletActivity.class);
            intent.putExtra(WalletActivity.INSTALLED_WALLET, installedWallet);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        }catch (Exception e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in selectWallet"));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void changeActivity(String activity, Object... objects) {
        if(activity.equals(Activities.CWP_WALLET_FACTORY_EDIT_WALLET.getCode())){
            Intent intent;
            try {


                intent = new Intent(this, com.bitdubai.android_core.app.EditableWalletActivity.class);
                intent.putExtra(EditableWalletActivity.WALLET_NAVIGATION_STRUCTURE,(WalletNavigationStructure)objects[0]);
                intent.putExtra(EditableWalletActivity.INSTALLED_WALLET,(InstalledWallet)objects[1]);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


            }catch (Exception e){
                getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in selectWallet"));
                Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
            }

        }else{
            try {
                //resetThisActivity();

                Activity a =  getSubAppRuntimeMiddleware().getLastSubApp().getActivity(Activities.getValueFromString(activity));

                loadUI(getSubAppSessionManager().getSubAppsSession(getSubAppRuntimeMiddleware().getLastSubApp().getType()));


            }catch (Exception e){

                getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in changeActivity"));
                Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void selectSubApp(InstalledSubApp installedSubApp) {
        Intent intent;

        try {

            //Activities activityType = Activities.getValueFromString(this.actionKey);

            SubApp subAppNavigationStructure= getSubAppRuntimeMiddleware().getSubApp(installedSubApp.getSubAppType());

            intent = new Intent(this, com.bitdubai.android_core.app.SubAppActivity.class);
            intent.putExtra(SubAppActivity.INSTALLED_SUB_APP, installedSubApp);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        }catch (Exception e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in selectWallet"));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void changeWalletFragment(String walletCategory, String walletType, String walletPublicKey, String fragmentType) {

    }

    @Override
    public void onCallbackViewObserver(FermatCallback fermatCallback) {

    }


    /**
     * Method that loads the UI
     */

    protected void loadUI(SubAppsSession subAppSession) {

        try {
            /**
             * Get current activity to paint
             */
            Activity activity = getActivityUsedType();

            loadBasicUI(activity);

            /**
             * Paint the screenPager
             */
            if (activity.getTabStrip() == null && activity.getFragments().size() > 1) {
                initialisePaging();
            }
            /**
             * Paint tabs
             */
            if (activity.getTabStrip() != null) {
                setPagerTabs(getSubAppRuntimeMiddleware().getLastSubApp(), activity.getTabStrip(), subAppSession);
            }
            /**
             * Paint single screen
             */
            if (activity.getFragments().size() == 1) {
                setOneFragmentInScreen();
            }
        }catch (NullPointerException e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                    Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setOneFragmentInScreen() throws InvalidParameterException {
        RelativeLayout relativeLayout = ((RelativeLayout) findViewById(R.id.only_fragment_container));
        SubAppRuntimeManager subAppRuntimeManager= getSubAppRuntimeMiddleware();
        SubApps subAppType = subAppRuntimeManager.getLastSubApp().getType();

        com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.SubAppFragmentFactory subAppFragmentFactory = SubAppFragmentFactory.getFragmentFactoryBySubAppType(subAppType);


        try {
            if(subAppFragmentFactory !=null){
                //TODO está linea está tirando error
                String fragment = subAppRuntimeManager.getLastSubApp().getLastActivity().getLastFragment().getType();
                SubAppsSession subAppsSession = getSubAppSessionManager().getSubAppsSession(subAppType);

                android.app.Fragment fragmet= subAppFragmentFactory.getFragment(fragment, subAppsSession, null,getSubAppResourcesProviderManager());
                FragmentTransaction FT = getFragmentManager().beginTransaction();
                FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                FT.replace(R.id.only_fragment_container, fragmet);
//                FT.addToBackStack(null);
//                FT.attach(fragmet);
//                FT.show(fragmet);
                FT.commit();

            }
        } catch (FragmentNotFoundException e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                    Toast.LENGTH_LONG).show();
        }

    }
    private SubAppsSession createOrCallSubAppSession(){
        SubAppsSession subAppSession = null;
        try {
            Bundle bundle = getIntent().getExtras();
            InstalledSubApp installedSubApp=null;
            if(bundle!=null){
                if(bundle.containsKey(INSTALLED_SUB_APP)){
                    installedSubApp  = (InstalledSubApp) bundle.getSerializable(INSTALLED_SUB_APP);
                }
            }
            if(installedSubApp!=null){
                if (getSubAppSessionManager().isSubAppOpen(installedSubApp.getSubAppType())) {
                    subAppSession = getSubAppSessionManager().getSubAppsSession(installedSubApp.getSubAppType());
                } else {
                    com.bitdubai.fermat_pip_api.layer.pip_module.developer.interfaces.ToolManager toolManager = getToolManager();
                    WalletStoreModuleManager walletStoreModuleManager = getWalletStoreModuleManager();
                    WalletPublisherModuleManager walletPublisherModuleManager = getWalletPublisherManager();
                    subAppSession = getSubAppSessionManager().openSubAppSession(installedSubApp.getSubAppType(), getErrorManager(), getWalletFactoryManager(), toolManager,walletStoreModuleManager,walletPublisherModuleManager,getIntraUserModuleManager(),getAssetFactoryModuleManager(), getCryptoBrokerIdentityModuleManager(), getIntraWalletUserManager());
                }
            }

        } catch (NullPointerException nullPointerException){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(nullPointerException));
        } catch (Exception e){
            e.printStackTrace();
            //this happend when is in home screen
        }
        return subAppSession;
    }

    /**
     * Called when an item in the navigation drawer is selected.
     *
     * @param position
     * @param activityCode
     */
    @Override
    public void onNavigationDrawerItemSelected(int position, String activityCode) {
        changeActivity(activityCode);
    }
}
