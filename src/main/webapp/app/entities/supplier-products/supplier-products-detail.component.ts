import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SupplierProducts } from './supplier-products.model';
import { SupplierProductsService } from './supplier-products.service';

@Component({
    selector: 'jhi-supplier-products-detail',
    templateUrl: './supplier-products-detail.component.html'
})
export class SupplierProductsDetailComponent implements OnInit, OnDestroy {

    supplierProducts: SupplierProducts;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private supplierProductsService: SupplierProductsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSupplierProducts();
    }

    load(id) {
        this.supplierProductsService.find(id).subscribe((supplierProducts) => {
            this.supplierProducts = supplierProducts;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSupplierProducts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'supplierProductsListModification',
            (response) => this.load(this.supplierProducts.id)
        );
    }
}
