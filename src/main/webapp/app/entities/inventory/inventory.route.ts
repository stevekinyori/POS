import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InventoryComponent } from './inventory.component';
import { InventoryDetailComponent } from './inventory-detail.component';
import { InventoryPopupComponent } from './inventory-dialog.component';
import { InventoryDeletePopupComponent } from './inventory-delete-dialog.component';

export const inventoryRoute: Routes = [
    {
        path: 'inventory',
        component: InventoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inventories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'inventory/:id',
        component: InventoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inventories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const inventoryPopupRoute: Routes = [
    {
        path: 'inventory-new',
        component: InventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'inventory/:id/edit',
        component: InventoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'inventory/:id/delete',
        component: InventoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inventories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
