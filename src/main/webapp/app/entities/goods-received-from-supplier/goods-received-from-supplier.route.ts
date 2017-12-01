import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GoodsReceivedFromSupplierComponent } from './goods-received-from-supplier.component';
import { GoodsReceivedFromSupplierDetailComponent } from './goods-received-from-supplier-detail.component';
import { GoodsReceivedFromSupplierPopupComponent } from './goods-received-from-supplier-dialog.component';
import { GoodsReceivedFromSupplierDeletePopupComponent } from './goods-received-from-supplier-delete-dialog.component';

export const goodsReceivedFromSupplierRoute: Routes = [
    {
        path: 'goods-received-from-supplier',
        component: GoodsReceivedFromSupplierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReceivedFromSuppliers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'goods-received-from-supplier/:id',
        component: GoodsReceivedFromSupplierDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReceivedFromSuppliers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const goodsReceivedFromSupplierPopupRoute: Routes = [
    {
        path: 'goods-received-from-supplier-new',
        component: GoodsReceivedFromSupplierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReceivedFromSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'goods-received-from-supplier/:id/edit',
        component: GoodsReceivedFromSupplierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReceivedFromSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'goods-received-from-supplier/:id/delete',
        component: GoodsReceivedFromSupplierDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GoodsReceivedFromSuppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
