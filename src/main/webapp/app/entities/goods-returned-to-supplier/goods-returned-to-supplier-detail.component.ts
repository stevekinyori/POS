import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { GoodsReturnedToSupplier } from './goods-returned-to-supplier.model';
import { GoodsReturnedToSupplierService } from './goods-returned-to-supplier.service';

@Component({
    selector: 'jhi-goods-returned-to-supplier-detail',
    templateUrl: './goods-returned-to-supplier-detail.component.html'
})
export class GoodsReturnedToSupplierDetailComponent implements OnInit, OnDestroy {

    goodsReturnedToSupplier: GoodsReturnedToSupplier;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private goodsReturnedToSupplierService: GoodsReturnedToSupplierService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGoodsReturnedToSuppliers();
    }

    load(id) {
        this.goodsReturnedToSupplierService.find(id).subscribe((goodsReturnedToSupplier) => {
            this.goodsReturnedToSupplier = goodsReturnedToSupplier;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGoodsReturnedToSuppliers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'goodsReturnedToSupplierListModification',
            (response) => this.load(this.goodsReturnedToSupplier.id)
        );
    }
}
