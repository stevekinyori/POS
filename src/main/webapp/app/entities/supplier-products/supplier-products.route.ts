import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SupplierProductsComponent } from './supplier-products.component';
import { SupplierProductsDetailComponent } from './supplier-products-detail.component';
import { SupplierProductsPopupComponent } from './supplier-products-dialog.component';
import { SupplierProductsDeletePopupComponent } from './supplier-products-delete-dialog.component';

export const supplierProductsRoute: Routes = [
    {
        path: 'supplier-products',
        component: SupplierProductsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SupplierProducts'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'supplier-products/:id',
        component: SupplierProductsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SupplierProducts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const supplierProductsPopupRoute: Routes = [
    {
        path: 'supplier-products-new',
        component: SupplierProductsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SupplierProducts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'supplier-products/:id/edit',
        component: SupplierProductsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SupplierProducts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'supplier-products/:id/delete',
        component: SupplierProductsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SupplierProducts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
