import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GoodsReturnedToSupplierComponent } from './goods-returned-to-supplier.component';
import { GoodsReturnedToSupplierDetailComponent } from './goods-returned-to-supplier-detail.component';
import { GoodsReturnedToSupplierPopupComponent } from './goods-returned-to-supplier-dialog.component';
import { GoodsReturnedToSupplierDeletePopupComponent } from './goods-returned-to-supplier-delete-dialog.component';

export const goodsReturnedToSupplierRoute: Routes = [
    {
        path: 'goods-returned-to-supplier',
        component: GoodsReturnedToSupplierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReturnedToSuppliers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'goods-returned-to-supplier/:id',
        component: GoodsReturnedToSupplierDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReturnedToSuppliers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const goodsReturnedToSupplierPopupRoute: Routes = [
    {
        path: 'goods-returned-to-supplier-new',
        component: GoodsReturnedToSupplierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReturnedToSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'goods-returned-to-supplier/:id/edit',
        component: GoodsReturnedToSupplierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReturnedToSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'goods-returned-to-supplier/:id/delete',
        component: GoodsReturnedToSupplierDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReturnedToSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
