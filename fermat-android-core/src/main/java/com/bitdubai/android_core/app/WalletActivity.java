package com.bitdubai.android_core.app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bitdubai.fermat.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.ActivityType;
import com.bitdubai.fermat_android_api.layer.definition.wallet.exceptions.FragmentNotFoundException;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.WalletFragmentFactory;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.WalletSession;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Activity;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.WalletNavigationStructure;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatCallback;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatScreenSwapper;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.interfaces.WalletRuntimeManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.exceptions.WalletRuntimeExceptions;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantLoadWalletSettings;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.interfaces.WalletSettings;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledSubApp;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledWallet;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.fermat_api.layer.dmp_wallet_module.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedWalletExceptionSeverity;


/**
 * Created by Matias Furszyfer
 */


public class WalletActivity extends FermatActivity implements FermatScreenSwapper,com.bitdubai.android_core.app.common.version_1.navigation_drawer.NavigationDrawerFragment.NavigationDrawerCallbacks {


    public static final String INSTALLED_WALLET="installedWallet";

    public static final String WALLET_PUBLIC_KEY="walletPublicKey";
    private static final String WALLET_TYPE = "walletType";
    private static final String WALLET_CATEGORY = "walletCategory";

    private InstalledWallet lastWallet;



    /**
     *  Called when the activity is first created
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setActivityType(ActivityType.ACTIVITY_TYPE_WALLET);

        try {

            /*
            * Load wallet UI
            */

            loadUI(createOrCallWalletSession());

        } catch (Exception e) {
            getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, FermatException.wrapException(e));
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

        try {
            MenuInflater inflater = getMenuInflater();

            /**
             *  Our future code goes here...
             */

        }
        catch (Exception e) {
            getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }

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
        }
        catch (Exception e) {
            getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     *  Called to retrieve per-instance state from an activity before being killed so that the state can be restored in onCreate(Bundle) or onRestoreInstanceState(Bundle)
     * @param outState
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    /**
     * This method is called after onStart() when the activity is being re-initialized from a previously saved state, given here in savedInstanceState.
     * Most implementations will simply use onCreate(Bundle) to restore their state, but it is sometimes convenient to do it here after all of the initialization has been done or to allow subclasses to decide whether to use your default implementation
     * @param savedInstanceState
     */

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try{

            if(savedInstanceState==null){
                savedInstanceState=new Bundle();
            }else
            super.onRestoreInstanceState(savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     * Method call when back button is pressed
     */

    @Override
    public void onBackPressed() {
        // get actual fragment on execute
        String frgBackType = null;

        WalletRuntimeManager walletRuntimeManager = getWalletRuntimeManager();

        WalletNavigationStructure walletNavigationStructure = walletRuntimeManager.getLastWallet();

        Activity activity = walletNavigationStructure.getLastActivity();

        com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Fragment fragment  = activity.getLastFragment();

        if (fragment != null)  frgBackType = fragment.getBack();

        if (frgBackType != null) {

            com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Fragment fragmentBack =walletRuntimeManager.getLastWallet().getLastActivity().getFragment(fragment.getBack());

             //loadFragment(frgBackType);
            changeWalletFragment(walletNavigationStructure.getWalletCategory(),walletNavigationStructure.getWalletType(),walletNavigationStructure.getPublicKey(),frgBackType);



        }else if(activity!=null && activity.getBackActivity()!=null){
            changeActivity(activity.getBackActivity().getCode());
        }else{
            getSubAppRuntimeMiddleware().getSubApp(SubApps.CWP_WALLET_MANAGER);
            getSubAppRuntimeMiddleware().getLastSubApp().getActivity(Activities.CWP_WALLET_MANAGER_MAIN);
            resetThisActivity();
            Intent intent = new Intent(this, SubAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }


//        if (getWalletRuntimeManager().getLastWallet().getLastActivity().getType()!= Activities.CWP_WALLET_MANAGER_MAIN){
//            getSubAppRuntimeMiddleware().getSubApp(SubApps.CWP_WALLET_MANAGER);
//            getSubAppRuntimeMiddleware().getLastSubApp().getActivity(Activities.CWP_WALLET_MANAGER_MAIN);
//            resetThisActivity();
//
//            // TODO : Esto debe ir hacia la subAppActivity en caso de querer ver el home, en un futuro cuando se quiera ver la lista de walletAbiertas va ser distinto
//            Intent intent = new Intent(this, SubAppActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//        }else{
//            super.onBackPressed();
//        }
    }



    /**
     * Method that loads the UI
    */
    protected void loadUI(WalletSession walletSession) {

        try
        {
            /**
             * Selected wallet to paint
             */
            WalletNavigationStructure wallet= getWalletRuntimeManager().getLastWallet();

            /**
             * Get current activity to paint
             */
            Activity activity=wallet.getLastActivity();


            /**
             * Load screen basics returning PagerSlidingTabStrip to load fragments
             */
            loadBasicUI(activity);

            /**
             * Paint a simgle fragment
             */
            if(activity.getTabStrip() == null && activity.getFragments().size() > 1){
                initialisePaging();
            }
            if (activity.getTabStrip() !=null ){
                /**
                 * Paint tabs
                 */
                setPagerTabs(wallet, activity.getTabStrip(), walletSession);
            }
            if(activity.getFragments().size() == 1){
                setOneFragmentInScreen();
            }
        }
        catch (Exception e) {
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setOneFragmentInScreen(){
        WalletNavigationStructure walletRuntime= getWalletRuntimeManager().getLastWallet();
        String walletPublicKey = walletRuntime.getPublicKey();
        String walletCategory = walletRuntime.getWalletCategory();
        String walletType = walletRuntime.getWalletType();

        WalletFragmentFactory walletFragmentFactory = com.bitdubai.android_core.app.common.version_1.FragmentFactory.WalletFragmentFactory.getFragmentFactoryByWalletType(walletCategory,walletType,walletPublicKey);

        try {
            if(walletFragmentFactory !=null){
                String fragment = walletRuntime.getLastActivity().getLastFragment().getType();
                WalletSession walletSession = getWalletSessionManager().getWalletSession(walletPublicKey);
                android.app.Fragment fragmet= walletFragmentFactory.getFragment(fragment.toString(), walletSession,getWalletSettingsManager().getSettings(walletPublicKey),getWalletResourcesProviderManager());
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
        } catch (CantLoadWalletSettings cantLoadWalletSettings) {
            cantLoadWalletSettings.printStackTrace();
        }

    }

    private WalletSession createOrCallWalletSession(){
        WalletSession walletSession = null;
        try {
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null) {
                if (bundle.containsKey(INSTALLED_WALLET)) {
                    lastWallet = (InstalledWallet) bundle.getSerializable(INSTALLED_WALLET);
                }else if (bundle.containsKey(WALLET_PUBLIC_KEY)){
                    String walletPublicKey = (String) bundle.get(WALLET_PUBLIC_KEY);
                    lastWallet = getWalletManager().getInstalledWallet(walletPublicKey);
                }
                if (getWalletSessionManager().isWalletOpen(lastWallet.getWalletPublicKey())) {
                    walletSession = getWalletSessionManager().getWalletSession(lastWallet.getWalletPublicKey());
                } else {
                    WalletSettings walletSettings = getWalletSettingsManager().getSettings(lastWallet.getWalletPublicKey());
                    walletSession = getWalletSessionManager().openWalletSession(lastWallet, getCryptoWalletManager(),walletSettings, getWalletResourcesProviderManager(), getErrorManager(),getCryptoBrokerWalletModuleManager());
                }
            }
        }catch (Exception e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                    Toast.LENGTH_LONG).show();
        }

        return walletSession;
    }

    public CryptoWalletManager getCryptoWalletManager(){
        return (CryptoWalletManager) ((ApplicationSession)getApplication()).getFermatPlatform().getCorePlatformContext().getPlugin(Plugins.BITDUBAI_CRYPTO_WALLET_WALLET_MODULE);
    }

    /**
     *
     */

    public WalletResourcesProviderManager getWalletResourcesProviderManager(){
        return (WalletResourcesProviderManager) ((ApplicationSession)getApplication()).getFermatPlatform().getCorePlatformContext().getPlugin(Plugins.BITDUBAI_WALLET_RESOURCES_NETWORK_SERVICE);

    }

    @Override
    public void changeActivity(String activityName, Object... objects) {
//        Method m = null;
//        try {
//            m = StrictMode.class.getMethod("incrementExpectedActivityCount", Class.class);
//            m.invoke(null, WalletActivity.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        try {


            WalletNavigationStructure walletNavigationStructure = getWalletRuntimeManager().getLastWallet();

            Activity activity = walletNavigationStructure.getActivity(Activities.getValueFromString(activityName));



            Intent intent;

            try {

                //Activities activityType = Activities.getValueFromString(this.actionKey);


                //getWalletRuntimeManager().getWallet(getWalletRuntimeManager().getLastWallet().getPublicKey());



                intent = getIntent();//new Intent(this, com.bitdubai.android_core.app.WalletActivity.class);
                if(intent.hasExtra(WalletActivity.WALLET_PUBLIC_KEY)){
                    intent.removeExtra(WalletActivity.WALLET_PUBLIC_KEY);
                }
                if(intent.hasExtra(WalletActivity.INSTALLED_WALLET)){
                    intent.removeExtra(WalletActivity.INSTALLED_WALLET);
                }
                intent.putExtra(WalletActivity.WALLET_PUBLIC_KEY, getWalletRuntimeManager().getLastWallet().getPublicKey());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


                //finalize();
                System.gc();
                //overridePendingTransition(0, 0);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //this.finalize();

                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //startActivity(intent);


                for(int i=0;i<activity.getFragments().size();i++){
                    getFragmentManager().popBackStack();
                }

                //NavigationDrawerFragment.onDetach();
                NavigationDrawerFragment = null;




                final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading...",
                        true);
                dialog.show();


                resetThisActivity();

                System.gc();

                // Execute some code after 2 seconds have passed
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        recreate();
                    }
                }, 2000);


                //finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //System.gc();


            }catch (Exception e){
                getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in selectWallet"));
                Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
            } catch (Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Oooops! recovering from system error. Throwable", Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }

            //loadUI(getWalletSessionManager().getWalletSession());


        }catch (Exception e){

            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in changeActivity"));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.i("FERMAT MATI", "New intent with flags " + intent.getFlags());
        Log.i("FERMAT MATI", "New intent with flags " + intent.getFlags());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void selectSubApp(InstalledSubApp installedSubApp) {

    }

    @Override
    public void changeWalletFragment(String walletCategory, String walletType,String walletPublicKey, String fragmentType) {
        try {
            getWalletRuntimeManager().getLastWallet().getLastActivity().getFragment(fragmentType);
            WalletFragmentFactory walletFragmentFactory = com.bitdubai.android_core.app.common.version_1.FragmentFactory.WalletFragmentFactory.getFragmentFactoryByWalletType(walletCategory, walletType, walletPublicKey);
            Fragment fragment= walletFragmentFactory.getFragment(fragmentType, getWalletSessionManager().getWalletSession(getWalletRuntimeManager().getLastWallet().getPublicKey()),getWalletSettingsManager().getSettings(walletPublicKey), getWalletResourcesProviderManager());
            FragmentTransaction FT = this.getFragmentManager().beginTransaction();
            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            FT.replace(R.id.fragment_container2, fragment);
//            FT.addToBackStack(null);
//            FT.attach(fragment);
//            FT.show(fragment);
            FT.commit();
        } catch (Exception e){
            getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, new IllegalArgumentException("Error in changeWalletFragment"));
            Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCallbackViewObserver(FermatCallback fermatCallback) {

    }

    @Override
    public void changeScreen(String fragment, int containerId,Object[] objects) {


    }
    private void loadFragment(String fragmentType){

    }

    @Override
    public void selectWallet(InstalledWallet installedWallet) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        //outState.putSerializable(INSTALLED_WALLET, lastWallet);
        outState.putString(WALLET_PUBLIC_KEY,lastWallet.getWalletPublicKey());
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        try {
            String walletPublicKey = savedInstanceState.getString(WALLET_PUBLIC_KEY);

            getWalletRuntimeManager().getWallet(walletPublicKey);

        } catch (WalletRuntimeExceptions walletRuntimeExceptions) {
            walletRuntimeExceptions.printStackTrace();
        }
    }

    /**
     * Dispatch onStop() to all fragments.  Ensure all loaders are stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();

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
