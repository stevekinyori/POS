import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SaleTransactionsComponent } from './sale-transactions.component';
import { SaleTransactionsDetailComponent } from './sale-transactions-detail.component';
import { SaleTransactionsPopupComponent } from './sale-transactions-dialog.component';
import { SaleTransactionsDeletePopupComponent } from './sale-transactions-delete-dialog.component';

export const saleTransactionsRoute: Routes = [
    {
        path: 'sale-transactions',
        component: SaleTransactionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleTransactions'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sale-transactions/:id',
        component: SaleTransactionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleTransactions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleTransactionsPopupRoute: Routes = [
    {
        path: 'sale-transactions-new',
        component: SaleTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleTransactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-transactions/:id/edit',
        component: SaleTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleTransactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-transactions/:id/delete',
        component: SaleTransactionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleTransactions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
