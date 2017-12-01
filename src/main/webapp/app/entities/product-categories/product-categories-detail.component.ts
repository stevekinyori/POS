import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ProductCategories } from './product-categories.model';
import { ProductCategoriesService } from './product-categories.service';

@Component({
    selector: 'jhi-product-categories-detail',
    templateUrl: './product-categories-detail.component.html'
})
export class ProductCategoriesDetailComponent implements OnInit, OnDestroy {

    productCategories: ProductCategories;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private productCategoriesService: ProductCategoriesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProductCategories();
    }

    load(id) {
        this.productCategoriesService.find(id).subscribe((productCategories) => {
            this.productCategories = productCategories;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProductCategories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'productCategoriesListModification',
            (response) => this.load(this.productCategories.id)
        );
    }
}
