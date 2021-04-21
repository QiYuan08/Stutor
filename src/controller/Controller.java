package controller;

import interfaces.InputInterface;
import interfaces.ResponseUtil;
import utils.ResponseOpenBidUtil;

public abstract class Controller {

    protected InputInterface inputPage;
    protected ResponseUtil utils;

    Controller(InputInterface inputPage){
        this.inputPage = inputPage;
        initListener();
    }

    public void initListener(){
    }
}
