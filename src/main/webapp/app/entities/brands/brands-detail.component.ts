import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Brands } from './brands.model';
import { BrandsService } from './brands.service';

@Component({
    selector: 'jhi-brands-detail',
    templateUrl: './brands-detail.component.html'
})
export class BrandsDetailComponent implements OnInit, OnDestroy {

    brands: Brands;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private brandsService: BrandsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBrands();
    }

    load(id) {
        this.brandsService.find(id).subscribe((brands) => {
            this.brands = brands;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBrands() {
        this.eventSubscriber = this.eventManager.subscribe(
            'brandsListModification',
            (response) => this.load(this.brands.id)
        );
    }
}
