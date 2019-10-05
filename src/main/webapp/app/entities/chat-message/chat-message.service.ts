import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatMessage } from 'app/shared/model/chat-message.model';

type EntityResponseType = HttpResponse<IChatMessage>;
type EntityArrayResponseType = HttpResponse<IChatMessage[]>;

@Injectable({ providedIn: 'root' })
export class ChatMessageService {
  public resourceUrl = SERVER_API_URL + 'api/chat-messages';

  constructor(protected http: HttpClient) {}

  create(chatMessage: IChatMessage): Observable<EntityResponseType> {
    return this.http.post<IChatMessage>(this.resourceUrl, chatMessage, { observe: 'response' });
  }

  update(chatMessage: IChatMessage): Observable<EntityResponseType> {
    return this.http.put<IChatMessage>(this.resourceUrl, chatMessage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChatMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChatMessage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
